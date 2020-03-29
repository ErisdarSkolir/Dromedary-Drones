package edu.gcc.xml;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.SystemUtils;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import com.ximpleware.ModifyException;

import edu.gcc.util.ReflectionUtils;
import edu.gcc.xml.annotation.XmlSerializable;
import edu.gcc.xml.exception.XmlDeserializeException;
import edu.gcc.xml.exception.XmlSchemaCreationException;
import edu.gcc.xml.exception.XmlSerializationException;

/**
 * @author Luke Donmoyer
 *
 *         This class provides an interface between an {@link XmlFile} and the
 *         objects to be stored in the file.
 *
 * @param <T> The object type to be stored in the {@link XmlFile}
 */
public class XmlSchema<T> {
	private static final String DATABASE_FOLDER = String.format("%s/DromedaryDrones/db/",
			SystemUtils.IS_OS_WINDOWS ? System.getenv("APPDATA") : System.getProperty("user.home"));

	private String elementName;
	private String primaryKeyName;

	private Function<T, String> funGetPrimaryKey;
	private Function<T, String> funObjectToXml;
	private Function<Map<String, String>, T> funXmlToObject;

	private List<XmlReactive<T>> singleReactives = new ArrayList<>();
	private List<XmlReactive<List<T>>> listReactives = new ArrayList<>();

	private XmlFile xmlFile;

	/**
	 * Static initializer class that provides type safety.
	 * 
	 * @param <A>   The class type.
	 * @param clazz The class type
	 * @return A new Schema with the generic parameter <A>
	 */
	public static <A> XmlSchema<A> of(final Class<A> clazz) {
		return new XmlSchema<>(clazz);
	}

	/**
	 * Private constructor. Creates a new schema for the provided object type. The
	 * directory that the XML files should be stored in will be created if it does
	 * not already exist.
	 * 
	 * @param clazz The object type to be stored in this schema.
	 */
	private XmlSchema(final Class<T> clazz) {
		if (!clazz.isAnnotationPresent(XmlSerializable.class))
			throw new IllegalArgumentException("Class must be annotated with XmlSerializable");

		elementName = clazz.getSimpleName();
		primaryKeyName = clazz.getAnnotation(XmlSerializable.class).value();

		funGetPrimaryKey = curryGetPrimaryKey(clazz);
		funObjectToXml = curryFunObjectToXml(clazz);
		funXmlToObject = curryFunXmlToObject(clazz);

		Path directory = Paths.get(DATABASE_FOLDER);
		Path file = Paths.get(String.format("%s%s.xml", DATABASE_FOLDER, clazz.getSimpleName()));

		try {
			if (Files.notExists(directory))
				Files.createDirectories(directory);

			xmlFile = new XmlFile(file.toString());
		} catch (IOException | ModifyException e) {
			throw new XmlSchemaCreationException("Could not create xml file or directory");
		}
	}

	/**
	 * Inserts the given object into the XML file. Any reactives that match the
	 * given object will be updated.
	 * 
	 * @param value The object to be inserted.
	 */
	public final void insert(final T value) {
		if (xmlFile.containsElement(getPrimaryKeyQuery(value)))
			throw new IllegalArgumentException("Cannot insert element that already exists");

		String element = funObjectToXml.apply(value);
		xmlFile.insertElementAtEnd(element);

		checkReactives(element);
	}

	/**
	 * Updates the given object that is already in the XML file. Any reactives that
	 * match the given object will also be updated.
	 * 
	 * @param value The object to be updated.
	 * @return True if the update was successful, otherwise false.
	 */
	public final boolean update(final T value) {
		String element = funObjectToXml.apply(value);
		boolean result = xmlFile.updateElement(getPrimaryKeyQuery(value), element);

		checkReactives(element);

		return result;
	}

	/**
	 * Deletes the given object from the XML file. Any reactives that match the
	 * given object will also be updated.
	 * 
	 * @param value The value to be deleted.
	 * @return True if the value was successfully deleted, otherwise false.
	 */
	public final boolean delete(final T value) {
		boolean result = xmlFile.removeElement(getPrimaryKeyQuery(value));

		checkReactives(funObjectToXml.apply(value));

		return result;
	}

	/**
	 * Returns an {@link XmlReactive} with a single object in it that matches the
	 * given xPath query.
	 * 
	 * @param xPath The xPath query to evaluate.
	 * @return An {@link XmlReactive} with the object.
	 */
	public final XmlReactive<T> getSingleReactive(final String xPath) {
		XmlReactive<T> rx = new XmlReactive<>(this::get, xPath);
		singleReactives.add(rx);
		return rx;
	}

	/**
	 * Returns an {@link XmlReactive} with a list of objects that match the given
	 * xPath query.
	 * 
	 * @param xPath The xPath query to evaluate.
	 * @return An {@link XmlReactive} with a list of objects.
	 */
	public final XmlReactive<List<T>> getListReactive(final String xPath) {
		XmlReactive<List<T>> rx = new XmlReactive<>(this::getList, xPath);
		listReactives.add(rx);
		return rx;
	}

	/**
	 * Returns a single object that matches the given xPath query.
	 * 
	 * @param xPath The xPath query to evaluate.
	 * @return The object that matches to xPath query.
	 */
	public final T get(final String xPath) {
		XmlElementIterator iterator = xmlFile.iterator(xPath);

		if (iterator.hasNext())
			return funXmlToObject.apply(iterator.next());

		throw new IllegalArgumentException(String.format("XmlFile does not contian element that matches %s", xPath));
	}

	/**
	 * Returns a list of objects that match the given xPath query.
	 * 
	 * @param xPath The xPath query to evaluate.
	 * @return A list of objects that match the xPath query.
	 */
	public final List<T> getList(final String xPath) {
		Iterable<Map<String, String>> iterable = () -> xmlFile.iterator(xPath);

		return StreamSupport.stream(iterable.spliterator(), false).map(funXmlToObject::apply)
				.collect(Collectors.toList());
	}

	/**
	 * Blocks and forces the underlying {@link XmlFile} to finish any writes.
	 */
	public final void flush() {
		xmlFile.flush();
	}

	/**
	 * Checks if the given XML element matches any reactives and if so notifies it
	 * of a change.
	 * 
	 * @param element The element to test against any reactives.
	 */
	private final void checkReactives(final String element) {
		for (XmlReactive<T> rx : singleReactives) {
			if (rx.matches(element))
				rx.notifyListeners();
		}

		for (XmlReactive<List<T>> rx : listReactives) {
			if (rx.matches(element))
				rx.notifyListeners();
		}
	}

	/**
	 * Returns an xPath query that matches an object with the given object's primary
	 * key. Thus the query returned by this method only works for the specified
	 * object since primary keys are unique.
	 * 
	 * @param value The object to get an xPath query for.
	 * @return An xPath query that matches the given object.
	 */
	private final String getPrimaryKeyQuery(final T value) {
		return String.format("//%s[%s[text()='%s']]", elementName, primaryKeyName, funGetPrimaryKey.apply(value));
	}

	/**
	 * Creates a function that accepts an object and returns the primary key as a
	 * string for serialization. The primary key must be a primitive, primitive
	 * wrapper, or a string. Any objects will only be turned into a string but they
	 * will not be able to be retrieved.
	 * 
	 * @param clazz The type of the object to be serialized.
	 * @return A function that turns an object into a string containing its primary
	 *         key.
	 */
	private final Function<T, String> curryGetPrimaryKey(final Class<T> clazz) {
		MethodHandles.Lookup lookup = MethodHandles.lookup();

		try {
			MethodHandle getter = ReflectionUtils.getFieldGetter(clazz, primaryKeyName, lookup);
			return instance -> {
				try {
					return getter.invoke(instance).toString();
				} catch (Throwable e) {
					throw new XmlSerializationException("Failed to get primary key", e);
				}
			};
		} catch (Exception e) {
			throw new XmlSchemaCreationException("Could not access primary key, it may not exist or is named wrong", e);
		}
	}

	/**
	 * Creates a function that accepts an object and returns a string that represent
	 * the class as an XML element. Any transient fields will be ignored and the
	 * reset must be primitives, primitive wrappers, or strings.
	 * 
	 * @param clazz The type of the object to be serialized.
	 * @return A function that turns an object into an XML element.
	 */
	private final Function<T, String> curryFunObjectToXml(final Class<T> clazz) {
		XMLOutputFactory xmlFactory = XMLOutputFactory.newInstance();
		Map<String, MethodHandle> getters = new HashMap<>();
		MethodHandles.Lookup lookup = MethodHandles.lookup();

		try {
			for (Field field : clazz.getDeclaredFields()) {
				if (!Modifier.isTransient(field.getModifiers())) {
					String fieldName = field.getName();
					getters.put(fieldName, ReflectionUtils.getFieldGetter(clazz, fieldName, lookup));
				}
			}
		} catch (Exception e) {
			throw new XmlSchemaCreationException("Could not get field getter");
		}

		return instance -> {
			StringWriter stringWriter = new StringWriter();

			try {
				XMLStreamWriter xmlWriter = xmlFactory.createXMLStreamWriter(stringWriter);
				xmlWriter.writeStartElement(elementName);

				for (Map.Entry<String, MethodHandle> entry : getters.entrySet()) {
					xmlWriter.writeStartElement(entry.getKey());
					xmlWriter.writeCharacters(entry.getValue().invoke(instance).toString());
					xmlWriter.writeEndElement();
				}

				xmlWriter.writeEndElement();
				xmlWriter.close();
			} catch (Throwable e) {
				throw new XmlSerializationException(String.format("Failed to created xml for class %s", clazz));
			}

			return stringWriter.toString();
		};
	}

	/**
	 * Creates a function that accepts a map of field names to values and returns an
	 * object with those values. Any fields must be primitives, primitive wrappers,
	 * or strings. Nested objects are not supported.
	 * 
	 * @param clazz The type of the object to be deserialized.
	 * @return A function that deserializes an object.
	 */
	private final Function<Map<String, String>, T> curryFunXmlToObject(final Class<T> clazz) {
		Map<String, BiConsumer<String, Object>> setters = new HashMap<>();
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		ObjectInstantiator<T> instantiator = new ObjenesisStd().getInstantiatorOf(clazz);

		try {
			for (Field field : clazz.getDeclaredFields()) {
				if (!Modifier.isTransient(field.getModifiers())) {
					String fieldName = field.getName();
					MethodHandle setter = ReflectionUtils.getFieldSetter(clazz, fieldName, lookup);
					Function<String, Object> deserializer = curryFieldDeserializer(clazz, fieldName);

					setters.put(fieldName, (input, instance) -> {
						try {
							setter.invoke(instance, deserializer.apply(input));
						} catch (Throwable e) {
							throw new XmlSchemaCreationException(String.format("Failed to set field %s", fieldName));
						}
					});
				}
			}
		} catch (Exception e) {
			throw new XmlSchemaCreationException("Could not get field setters");
		}

		return values -> {
			try {
				T result = instantiator.newInstance();

				for (Map.Entry<String, String> entry : values.entrySet()) {
					setters.get(entry.getKey()).accept(entry.getValue(), result);
				}

				return result;
			} catch (Exception e) {
				throw new XmlDeserializeException(String.format("Failed to deserialize object %s", clazz), e);
			}
		};
	}

	/**
	 * Returns a function that turns a string into the object type of the given
	 * object's field. Field must be primitive, primitive wrapper, or a string.
	 * Nested objects are not currently supported.
	 * 
	 * @param clazz     The object type to get field type information.
	 * @param fieldName The field to create a parser for.
	 * @return A parser from a string to the given field's type.
	 */
	private final Function<String, Object> curryFieldDeserializer(final Class<T> clazz, final String fieldName) {
		try {
			Class<?> fieldType = clazz.getDeclaredField(fieldName).getType();
			return ClassUtils.isPrimitiveOrWrapper(fieldType) ? curryStringParse(fieldType) : s -> s;
		} catch (NoSuchFieldException e) {
			throw new XmlSchemaCreationException(
					String.format("Could not create field getter for class: %s field: %s", clazz, fieldName), e);
		}
	}

	/**
	 * Determines the type of the given class and returns the respective string
	 * parsing function. Given class must be a primitive, primitive wrapper. Other
	 * object types are not supported.
	 * 
	 * @param clazz The class to find a parsing function for.
	 * @return The parsing function for the given type.
	 * 
	 * @throws IllegalArgumentException If the given class is not a primitive or a
	 *                                  primitive wrapper.
	 */
	private final Function<String, Object> curryStringParse(final Class<?> clazz) {
		if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
			return Integer::parseInt;
		} else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
			return Double::parseDouble;
		} else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
			return Boolean::parseBoolean;
		} else if (clazz.equals(Short.class) || clazz.equals(short.class)) {
			return Short::parseShort;
		} else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
			return Long::parseLong;
		} else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
			return Float::parseFloat;
		} else if (clazz.equals(Byte.class) || clazz.equals(byte.class)) {
			return Byte::parseByte;
		} else if (clazz.equals(Character.class) || clazz.equals(char.class)) {
			return s -> s.charAt(0);
		} else {
			throw new IllegalArgumentException(clazz.getName() + " is not a primitive or wrapper class");
		}
	}
}