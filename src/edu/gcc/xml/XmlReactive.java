package edu.gcc.xml;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;

import edu.gcc.xml.exception.XmlReadException;

/**
 * @author Luke Donmoyer
 *
 *         This class represents a query to an XML file that can change over
 *         time. Whenever a new element is inserted, updated, or removed from
 *         the XML file, the listeners on any XmlReactive that matches the new
 *         element will be called with the new object or list.
 *
 * @param <T> The object or list of objects returned by the XMl query.
 */
public class XmlReactive<T> {
	private T value;
	private Function<String, T> valueFunction;
	private XPathExpression xPath;
	private String query;

	private List<XmlListener> listeners = new ArrayList<>();

	/**
	 * Main Constructor.
	 * 
	 * @param valueFunction The function that generates this reactive's value,
	 *                      usually a get function from an {@link XmlFile}.
	 * @param query         The xPath query to be matches against.
	 */
	public XmlReactive(final Function<String, T> valueFunction, final String query) {
		this.value = valueFunction.apply(query);
		this.valueFunction = valueFunction;
		this.query = query;

		try {
			this.xPath = XPathFactory.newInstance().newXPath().compile(query);
		} catch (XPathExpressionException e) {
			throw new XmlReadException(String.format("Could not compile xPath for reative %s", xPath), e);
		}
	}

	/**
	 * Returns the current value of this reactive.
	 * 
	 * @return the current value
	 */
	public T getValue() {
		return value;
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

	/**
	 * Updates the current value of this reactive using the value function and calls
	 * all listeners.
	 */
	public void notifyListeners() {
		this.value = valueFunction.apply(query);

		for (XmlListener listener : listeners) {
			listener.notify(value);
		}
	}

	/**
	 * Adds a listener to this reactive.
	 * 
	 * @param listener The listener to add.
	 */
	public void addListener(final XmlListener listener) {
		this.listeners.add(listener);
	}
}
