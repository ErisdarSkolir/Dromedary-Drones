package edu.gcc.xml.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Luke Donmoyer
 *
 *         Marks an XML DAO interface. Only a single class can be inserted,
 *         updated, or queried from this interface.
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface XmlDao {
	/**
	 * The class to be returned by this DAO.
	 */
	Class<?> value();
}
