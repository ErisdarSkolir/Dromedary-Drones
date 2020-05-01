package edu.gcc.xml.serializers;

import javax.xml.stream.XMLStreamException;

import com.ximpleware.NavException;
import com.ximpleware.VTDNav;

public class DoubleSerializer extends XmlFieldSerializer {
	@Override
	public Class<?>[] getClassesForSerializer() {
		return new Class<?>[] { Double.class, double.class };
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
			return Double.parseDouble(navigator.toString(value));

		throw new NavException("This element does not have text");
	}
}
