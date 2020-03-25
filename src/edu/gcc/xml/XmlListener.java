package edu.gcc.xml;

@FunctionalInterface
public interface XmlListener {
	<T> void notify(T newValue);
}
