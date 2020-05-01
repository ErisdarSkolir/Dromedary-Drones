package edu.gcc.xml.serializers;

import javax.xml.stream.XMLStreamException;

import com.ximpleware.NavException;
import com.ximpleware.VTDNav;

public class LongSerializer extends XmlFieldSerializer {
	@Override
	public Class<?>[] getClassesForSerializer() {
		return new Class<?>[] { Long.class, long.class };
	}

	@Override
	protected void subclassToXML(final Object object)
			throws XMLStreamException {
		xmlWriter.writeCharacters(object.toString());
	}

	@Override
	protected Object subclassToObject(final VTDNav navigator)
			throws NavException {
		navigator.toElement(VTDNav.FIRST_CHILD);

		int value = navigator.getText();

		if (value != -1)
			return Long.parseLong(navigator.toString(value));

		throw new NavException("This element does not have text");
	}
}
