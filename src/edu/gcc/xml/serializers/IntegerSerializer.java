package edu.gcc.xml.serializers;

import javax.xml.stream.XMLStreamException;

public class IntegerSerializer extends XmlFieldSerializer {
	@Override
	public Class<?>[] getClassesForSerializer() {
		return new Class<?>[] { Integer.class, int.class };
	}

	@Override
	protected void subclassToXML(final String elementName, final Object object) throws XMLStreamException {
		xmlWriter.writeStartElement(elementName);
		xmlWriter.writeCharacters(object.toString());
		xmlWriter.writeEndElement();
		xmlWriter.close();
	}

	@Override
	protected Object subclassToObject(String xml) {
		// TODO Auto-generated method stub
		return null;
	}
}
