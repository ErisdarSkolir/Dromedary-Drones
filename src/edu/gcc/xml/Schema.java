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

public class Schema<T> {
	private static String DATABASE_FOLDER = String.format("%s/DromedaryDrones/db/",
			SystemUtils.IS_OS_WINDOWS ? System.getenv("APPDATA") : System.getProperty("user.home"));

	private String elementName;
	private String primaryKeyName;

	private Function<T, String> funGetPrimaryKey;
	private Function<T, String> funObjectToXml;
	private Function<Map<String, String>, T> funXmlToObject;

	private XmlFile xmlFile;

	public static <A> Schema<A> of(final Class<A> clazz){
		return new Schema<>(clazz);
	}
	
	public Schema(final Class<T> clazz) {
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

			if (Files.notExists(file)) {
				Files.createFile(file);
				Files.write(file, "<root></root>".getBytes());
			}
		} catch (IOException e) {
			throw new RuntimeException("Could not create database folder");
		}

		try {
			xmlFile = new XmlFile(file.toString());
		} catch (IOException | ModifyException e) {
			throw new RuntimeException("Could not create xml file");
		}
	}

	public final void insert(final T value) {
		if (xmlFile.containsElement(getPrimaryKeyQuery(value)))
			throw new RuntimeException("Cannot insert element that already exists");

		xmlFile.insertElementAtEnd(funObjectToXml.apply(value));
	}

	public final boolean update(final T value) {
		return xmlFile.updateElement(getPrimaryKeyQuery(value), funObjectToXml.apply(value));
	}

	public final boolean delete(final T value) {
		return xmlFile.removeElement(getPrimaryKeyQuery(value));
	}

	public final T get(final String xPath) {
		XmlElementIterator iterator = xmlFile.iterator(xPath);

		if (iterator.hasNext())
			return funXmlToObject.apply(iterator.next());

		throw new IllegalArgumentException(String.format("XmlFile does not contian element that matches %s", xPath));
	}

	public final List<T> getList(final String xPath) {
		Iterable<Map<String, String>> iterable = () -> xmlFile.iterator(xPath);

		return StreamSupport.stream(iterable.spliterator(), false).map(funXmlToObject::apply)
				.collect(Collectors.toList());
	}

	public final void flush() {
		xmlFile.flush();
	}

	private final String getPrimaryKeyQuery(final T value) {
		return String.format("//%s[%s[text()='%s']]", elementName, primaryKeyName, funGetPrimaryKey.apply(value));
	}

	private final Function<T, String> curryGetPrimaryKey(final Class<T> clazz) {
		MethodHandles.Lookup lookup = MethodHandles.lookup();

		try {
			MethodHandle getter = ReflectionUtils.getFieldGetter(clazz, primaryKeyName, lookup);
			return instance -> {
				try {
					return getter.invoke(instance).toString();
				} catch (Throwable e) {
					throw new RuntimeException("Failed to get primary key", e);
				}
			};
		} catch (Exception e) {
			throw new RuntimeException("Could not access primary key, it may not exist or is named wrong", e);
		}
	}

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
			throw new RuntimeException("Could not get field getter");
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
				throw new RuntimeException(String.format("Failed to created xml for class %s", clazz));
			}

			return stringWriter.toString();
		};
	}

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
							throw new RuntimeException(String.format("Failed to set field %s", fieldName));
						}
					});
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Could not get field setters");
		}

		return values -> {
			try {
				T result = instantiator.newInstance();

				for (Map.Entry<String, String> entry : values.entrySet()) {
					setters.get(entry.getKey()).accept(entry.getValue(), result);
				}

				return result;
			} catch (Exception e) {
				throw new RuntimeException(String.format("Failed to deserialize object %s", clazz), e);
			}
		};
	}

	private final Function<String, Object> curryFieldDeserializer(final Class<T> clazz, final String fieldName) {
		// TODO make this handle nested xml serializable fields
		try {
			Class<?> fieldType = clazz.getDeclaredField(fieldName).getType();
			return ClassUtils.isPrimitiveOrWrapper(fieldType) ? curryStringParse(fieldType) : s -> s;
		} catch (NoSuchFieldException e) {
			throw new RuntimeException("Could not create field getter: ", e);
		}
	}

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
