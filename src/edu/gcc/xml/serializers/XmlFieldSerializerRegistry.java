package edu.gcc.xml.serializers;

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;

public class XmlFieldSerializerRegistry {
	public static final SetMultimap<Class<?>, XmlFieldSerializer> serializers = MultimapBuilder
			.hashKeys()
			.linkedHashSetValues(1)
			.build();

	static {
		registerSerializer(new IntegerSerializer());
		registerSerializer(new DoubleSerializer());
		registerSerializer(new FloatSerializer());
		registerSerializer(new LongSerializer());
		registerSerializer(new ShortSerializer());
		registerSerializer(new CharacterSerializer());
		registerSerializer(new StringSerializer());
		registerSerializer(new BooleanSerializer());
	}

	public static XmlFieldSerializer getSerializer(final Class<?> clazz) {
		return serializers.get(clazz).iterator().next();
	}
	
	public static void registerSerializer(final XmlFieldSerializer serializer) {
		Class<?>[] classes = serializer.getClassesForSerializer();

		for (Class<?> clazz : classes) {
			if (serializers.containsKey(clazz))
				throw new IllegalArgumentException(
						String.format(
							"Serializer already registered for %s",
							clazz
						)
				);

			serializers.put(clazz, serializer);
		}
	}
}
