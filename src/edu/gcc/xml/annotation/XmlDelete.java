package edu.gcc.xml.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Luke Donmoyer
 *
 *         Marks a method in an XML DAO as a delete query.
 */
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface XmlDelete {

}
