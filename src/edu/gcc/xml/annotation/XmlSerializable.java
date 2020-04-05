package edu.gcc.xml.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Luke Donmoyer
 * 
 *         Marks an object that it can be serialized in an XML file.
 *
 */
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface XmlSerializable {
	/**
	 * The name of the field that serves as the primary key. Must be unique.
	 */
	String value();

	/**
	 * If true and the primary key is an integer, long, or short then it will
	 * automatically be generated.
	 */
	boolean autogenerate() default false;
}
