package edu.gcc.xml.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Luke Donmoyer
 * 
 *         Marks a method in an XML DAO as a query.
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface XPathQuery {
	/**
	 * The XPath expression that is executed by this method. Any arguments to the
	 * method are denoted in the query with "{x}" where x denotes the ordinal
	 * position of parameters starting with 0.
	 * 
	 * Example:
	 * 		@XPathQuery("//book[@id='{0}']") 
	 * 		public Book getBookWhereID(int id);
	 */
	String value();

	/**
	 * Denotes whether this query is asynchronous. If true the method will return a
	 * {@link java.util.concurrent.CompletableFuture} with either a list of or a
	 * single object.
	 */
	boolean asynchronous() default false;

	/**
	 * Denotes whether this query returns a list or a single object. Can be combined
	 * with asynchronous.
	 */
	boolean list() default false;
}
