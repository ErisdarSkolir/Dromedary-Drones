package edu.gcc.xml;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;

public class XmlReactive<T> {
	private T value;
	private Function<String, T> valueFunction;
	private XPathExpression xPath;
	private String query;

	private List<XmlListener> listeners = new ArrayList<>();

	public XmlReactive(final Function<String, T> valueFunction, final String query) {
		this.value = valueFunction.apply(query);
		this.valueFunction = valueFunction;
		this.query = query;

		try {
			this.xPath = XPathFactory.newInstance().newXPath().compile(query);
		} catch (XPathExpressionException e) {
			throw new RuntimeException("Could not compile xPath " + xPath);
		}
	}

	public T getValue() {
		return value;
	}
	
	public boolean matches(final String element) {
		try{
			InputSource xml = new InputSource(new StringReader(element));
			return xPath.evaluate(xml) != null;
		} catch (XPathExpressionException e) {
			throw new RuntimeException("Could not evaulate xPath");
		}
	}

	public void notifyListeners() {
		this.value = valueFunction.apply(query);
		
		for (XmlListener listener : listeners) {
			listener.notify(value);
		}
	}

	public void addListener(final XmlListener listener) {
		this.listeners.add(listener);
	}
}
