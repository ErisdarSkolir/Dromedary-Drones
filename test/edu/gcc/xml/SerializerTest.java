package edu.gcc.xml;

import org.junit.jupiter.api.Test;

import edu.gcc.xml.serializers.XmlFieldSerializer;
import edu.gcc.xml.serializers.XmlFieldSerializerRegistry;

public class SerializerTest {
	@Test
	public void test() {
		XmlFieldSerializer intSerializer = XmlFieldSerializerRegistry
				.getSerializer(String.class);

		String xml = intSerializer.objToXml("Test", "Hello, World");

		System.out.println(xml);

		System.out.println(intSerializer.xmlToObj(xml));
	}
}
