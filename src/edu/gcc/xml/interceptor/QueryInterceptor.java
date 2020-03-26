package edu.gcc.xml.interceptor;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

import edu.gcc.xml.XmlSchema;
import edu.gcc.xml.annotation.XPathQuery;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

/**
 * @author Luke Donmoyer
 * 
 *         Method interceptor for XML queries.
 *
 * @param <T> The schema type.
 */
public class QueryInterceptor<T> {
	private XmlSchema<T> schema;

	public QueryInterceptor(final XmlSchema<T> schema) {
		this.schema = schema;
	}

	/**
	 * The method delegate for get queries. Calls the the get method in schema with
	 * the given parameter and wraps it in a CompletableFuture if the original
	 * method was marked as asynchronous. It also will return a list or a reactive
	 * if those are marked in the annotation.
	 * 
	 * @param method The original Method signature. Used to check annotations.
	 * @param ars    The parameters for the xPath query.
	 * @return An object. May be wrapped in a CompletableFuture, a List, an
	 *         XmlReactive or any combination thereof.
	 */
	@RuntimeType
	public Object get(@Origin Method method, @AllArguments Object[] args) {
		XPathQuery annotation = method.getAnnotation(XPathQuery.class);
		String query = replaceArguments(annotation.value(), args);

		return asynchronousOrBlocking(annotation, query);
	}

	/**
	 * Checks whether the annotation specifies this method as asynchronous and
	 * returns either returns a CompletableFuture or blocks with the normal method
	 * call.
	 * 
	 * @param annotation The annotation that specifies whether this method is
	 *                   asynchronous.
	 * @param query      The xPath query.
	 * @return A CompletableFuture or an Object.
	 */
	public Object asynchronousOrBlocking(final XPathQuery annotation, final String query) {
		return annotation.async() ? CompletableFuture.supplyAsync(() -> listOrSingle(annotation, query))
				: listOrSingle(annotation, query);
	}

	/**
	 * Checks whether the annotation specifies this method is a list or a single
	 * object.
	 * 
	 * @param annotation The annotation that specifies whether this method is a list
	 *                   or single object.
	 * @param query      The xPath query.
	 * @return A list of Objects or a single Object.
	 */
	public Object listOrSingle(final XPathQuery annotation, final String query) {
		return annotation.list() ? listOrReactive(annotation, query) : singleOrReactive(annotation, query);
	}

	/**
	 * Checks whether the annotation specifies this method should return a reactive
	 * with a single object or a regular object.
	 * 
	 * @param annotation The annotation that specifies whether this method is
	 *                   reactive or not.
	 * @param query      The xPath query.
	 * @return A reactive with a single Object or just a single Object.
	 */
	public Object singleOrReactive(final XPathQuery annotation, final String query) {
		return annotation.reactive() ? schema.getSingleReactive(query) : schema.get(query);
	}

	/**
	 * Checks whether the annotation specifies this method should return a reactive
	 * with a list or just a regular list.
	 * 
	 * @param annotation The annotation that specifies whether this method is
	 *                   reactive or not.
	 * @param query      The xPath query.
	 * @return A reactive with a list of Object or just a list or Objects.
	 */
	public Object listOrReactive(final XPathQuery annotation, final String query) {
		return annotation.reactive() ? schema.getListReactive(query) : schema.getList(query);
	}

	/**
	 * Replaces all arguments denoted with "{x}" where x is the ordinal number of
	 * the argument in the given xPath query.
	 * 
	 * @param xPath The query to be modified.
	 * @param args  The arguments to insert into the query.
	 * @return The new query with arguments replaced.
	 */
	private String replaceArguments(final String xPath, final Object[] args) {
		String query = xPath;

		for (int i = 0; i < args.length; i++) {
			query = query.replaceAll(String.format("(\\{%d\\})", i), args[i].toString());
		}

		return query;
	}
}
