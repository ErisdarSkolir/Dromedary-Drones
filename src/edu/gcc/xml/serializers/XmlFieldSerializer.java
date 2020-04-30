package edu.gcc.xml.serializers;

import java.io.StringWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import edu.gcc.xml.exception.XmlDeserializeException;
import edu.gcc.xml.exception.XmlSerializationException;

public abstract class XmlFieldSerializer {
	private static XMLOutputFactory xmlFactory = XMLOutputFactory.newInstance();

	private StringWriter stringWriter = new StringWriter();

	protected XMLStreamWriter xmlWriter;

	protected XmlFieldSerializer() {
		try {
			xmlWriter = xmlFactory.createXMLStreamWriter(stringWriter);
		} catch (Exception e) {
			throw new XmlSerializationException(
					"Failed to create xml factory",
					e
			);
		}
	}

	public abstract Class<?>[] getClassesForSerializer();

	public String objToXml(final String elementName, final Object object) {
		try {
			stringWriter.getBuffer().setLength(0);
			subclassToXML(elementName, object);
			return stringWriter.toString();
		} catch (Exception e) {
			throw new XmlSerializationException(
					String.format(
						"Failed to created xml for object %s",
						object
					),
					e
			);
		}
	}

	protected abstract void subclassToXML(
			final String elementName,
			final Object object
	) throws XMLStreamException;

	public Object xmlToObj(final String xml) {
		try {
			return subclassToObject(xml);
		} catch (Exception e) {
			throw new XmlDeserializeException(
					String.format(
						"Failed to deserialize object from XML string %s",
						xml
					),
					e
			);
		}
	}

	protected abstract Object subclassToObject(final String xml);
}
