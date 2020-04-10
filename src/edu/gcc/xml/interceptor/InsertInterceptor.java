package edu.gcc.xml.interceptor;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

import edu.gcc.xml.XmlSchema;
import edu.gcc.xml.annotation.XmlInsert;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

/**
 * @author Luke Donmoyer
 * 
 *         Method interceptor for XML inserts.
 *
 * @param <T> The schema type.
 */
public class InsertInterceptor<T> {
	private XmlSchema<T> schema;
	
	public InsertInterceptor(final XmlSchema<T> schema) {
		this.schema = schema;
	}
	
	/**
	 * The method delegate for inserts. Calls the the insert method in schema with
	 * the given parameter and wraps it in a CompletableFuture if the original method
	 * was marked as asynchronous.
	 * 
	 * @param method The original Method signature. Used to check annotations.
	 * @param object The object to be inserted into the schema.
	 * @return Void or a void CompleteableFuture
	 */
	@RuntimeType
	public Object insert(@Origin Method method, @Argument(0) final T object) {
		if(method.getAnnotation(XmlInsert.class).async())
			return CompletableFuture.runAsync(()-> schema.insert(object));
		
		schema.insert(object);
		
		return Void.TYPE;
	}
}
