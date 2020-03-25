package edu.gcc.xml.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Luke Donmoyer
 *
 *         Marks a method in an XML DAO as an update query.
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface XmlUpdate {
	/**
	 * Denotes whether this query is asynchronous. If true the method will return a
	 * {@link java.util.concurrent.CompletableFuture} with a boolean.
	 */
	boolean async() default false;
}
