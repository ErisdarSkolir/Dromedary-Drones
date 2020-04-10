package edu.gcc.xml;

import java.io.StringReader;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;

import edu.gcc.xml.exception.XmlReadException;
import javafx.beans.Observable;

/**
 * @author Luke Donmoyer
 *
 *         This class represents a query to an XML file that can change over
 *         time. Whenever a new element is inserted, updated, or removed from
 *         the XML file, the listeners on any XmlReactive that matches the new
 *         element will be called with the new object or list.
 *
 * @param <T> The object or list of objects returned by the XML query.
 */
public class XmlReactive<O extends Observable> {
	private O observable;
	private XPathExpression xPath;

	public XmlReactive(final O observable, final String query) {
		this.observable = observable;
		
		try {
			this.xPath = XPathFactory.newInstance().newXPath().compile(query);
		} catch (XPathExpressionException e) {
			throw new XmlReadException(String.format("Could not compile xPath for reative %s", xPath), e);
		}
	}
	
	public O getObservable() {
		return observable;
	}

	/**
	 * Checks whether the given element matches this reactive's xPath query.
	 * 
	 * @param element The element to check.
	 * @return true if the specified element should be included in the reactive
	 *         otherwise false.
	 */
	public boolean matches(final String element) {
		try {
			InputSource xml = new InputSource(new StringReader(element));
			return xPath.evaluate(xml) != null;
		} catch (XPathExpressionException e) {
			throw new XmlReadException(String.format("Could not evaulate element for reactive %s", element), e);
		}
	}
}
