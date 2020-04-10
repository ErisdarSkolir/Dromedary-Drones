package edu.gcc.xml.interceptor;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

import edu.gcc.xml.XmlSchema;
import edu.gcc.xml.annotation.XmlDelete;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

/**
 * @author Luke Donmoyer
 * 
 *         Method interceptor for XML deletes.
 *
 * @param <T> The schema type.
 */
public class DeleteInterceptor<T> {
	private XmlSchema<T> schema;

	public DeleteInterceptor(final XmlSchema<T> schema) {
		this.schema = schema;
	}

	/**
	 * The method delegate for deletes. Calls the the delete method in schema with
	 * the given parameter and wraps it in a CompletableFuture if the original method
	 * was marked as asynchronous.
	 * 
	 * @param method The original Method signature. Used to check annotations.
	 * @param object The object to be deleted from the schema.
	 * @return True if the delete was successful, otherwise false.
	 */
	@RuntimeType
	public Object delete(@Origin Method method, @Argument(0) final T object) {
		if (method.getAnnotation(XmlDelete.class).async())
			return CompletableFuture.supplyAsync(() -> schema.delete(object));

		return schema.delete(object);
	}
}
