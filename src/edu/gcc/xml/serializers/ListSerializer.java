package edu.gcc.xml.serializers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang3.ClassUtils;

import com.ximpleware.NavException;
import com.ximpleware.VTDNav;

public class ListSerializer extends XmlFieldSerializer {
	@Override
	public Class<?>[] getClassesForSerializer() {
		return new Class<?>[] { List.class, ArrayList.class, LinkedList.class };
	}

	@Override
	protected void subclassToXML(final Object object)
			throws XMLStreamException {
		if (!ClassUtils.isAssignable(object.getClass(), List.class))
			throw new IllegalArgumentException(
					"List serializer only accepts lists"
			);

		List<?> list = (List<?>) object;
		XmlFieldSerializer listObjectSerializer = XmlFieldSerializerRegistry.getSerializer(list.get(0).getClass());
		
		xmlWriter.writeAttribute("type", object.getClass().getCanonicalName());
		xmlWriter.writeAttribute("num_elements", Integer.toString(list.size()));
		
		for(Object listObject : list) {
			xmlWriter.writeAttribute
		}
	}

	@Override
	protected Object subclassToObject(VTDNav navigator) throws NavException {
		// TODO Auto-generated method stub
		return null;
	}
}
