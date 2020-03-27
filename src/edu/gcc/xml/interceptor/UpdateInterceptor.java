package edu.gcc.xml.interceptor;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

import edu.gcc.xml.XmlSchema;
import edu.gcc.xml.annotation.XmlUpdate;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

/**
 * @author Luke Donmoyer
 * 
 *         Method interceptor for XML updates.
 *
 * @param <T> The schema type.
 */
public class UpdateInterceptor<T> {
	private XmlSchema<T> schema;

	public UpdateInterceptor(final XmlSchema<T> schema) {
		this.schema = schema;
	}

	/**
	 * The method delegate for updates. Calls the the update method in schema with
	 * the given parameter and wraps it in a CompletableFuture if the original method
	 * was marked as asynchronous.
	 * 
	 * @param method The original Method signature. Used to check annotations.
	 * @param object The object to be updated in the schema.
	 * @return True if the update was successful, otherwise false.
	 */
	@RuntimeType
	public Object update(@Origin Method method, @Argument(0) final T object) {
		if (method.getAnnotation(XmlUpdate.class).async())
			return CompletableFuture.runAsync(() -> schema.update(object));

		return schema.update(object);
	}
}
