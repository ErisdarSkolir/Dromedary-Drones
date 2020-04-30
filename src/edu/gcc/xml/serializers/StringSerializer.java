package edu.gcc.xml.serializers;

import java.util.stream.Stream;

public class StringSerializer implements XmlFieldSerializer {
	@Override
	public Stream<Class<?>> getClassesForSerializer() {
		return Stream.of(String.class);
	}

	@Override
	public String toXML(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object toObject(String xml) {
		// TODO Auto-generated method stub
		return null;
	}
}
