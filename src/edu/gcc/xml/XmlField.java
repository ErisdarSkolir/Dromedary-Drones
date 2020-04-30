package edu.gcc.xml;

import java.lang.reflect.Field;
import java.util.function.Function;

public class XmlField {
	private Function<Object, String> serializer;
	private Function<String, Object> deserializer;
	
	public XmlField(final Field field) {
		
	}
}
