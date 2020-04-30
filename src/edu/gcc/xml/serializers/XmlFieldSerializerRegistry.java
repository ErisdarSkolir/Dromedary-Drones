package edu.gcc.xml.serializers;

import java.util.HashMap;
import java.util.Map;

public class XmlFieldSerializerRegistry {
	public static Map<Class<?>, XmlFieldSerializer<?>> serializers = new HashMap<>();
	
	static {
		registerSerializer(new IntegerSerializer());
	}
	
	public static <T> void registerSerializer(
			final XmlFieldSerializer<T> serializer
	) {
		Class<?> clazz = serializer.getClassForSerializer();

		if (serializers.containsKey(clazz))
			throw new IllegalArgumentException(
					String.format("Serializer already registered for %s", clazz)
			);
		
		serializers.put(clazz, serializer);
	}
}
