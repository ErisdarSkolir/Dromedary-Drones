package edu.gcc.xml.interceptor;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

import edu.gcc.xml.Schema;
import edu.gcc.xml.annotation.XmlDelete;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

public class DeleteInterceptor<T> {
	private Schema<T> schema;

	public DeleteInterceptor(final Schema<T> schema) {
		this.schema = schema;
	}

	@RuntimeType
	public Object delete(@Origin Method method, @Argument(0) final T object) {
		if(method.getAnnotation(XmlDelete.class).async())
			return CompletableFuture.supplyAsync(() -> schema.delete(object));
		
		return schema.delete(object);
	}
}
