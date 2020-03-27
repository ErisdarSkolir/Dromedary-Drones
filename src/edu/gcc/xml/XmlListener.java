package edu.gcc.xml;

/**
 * @author Luke Donmoyer
 * 
 *         Functional interfacer that represents a XmlReactive listener.
 *
 */
@FunctionalInterface
public interface XmlListener {
	/**
	 * The function to be called whenever an XmlReactive changes. Nothing should be
	 * returned by this method.
	 * 
	 * @param <T>      The type of the new value.
	 * @param newValue The actual new value that was recently updated.
	 */
	<T> void notify(T newValue);
}
