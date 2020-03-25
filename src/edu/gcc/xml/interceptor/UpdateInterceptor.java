package edu.gcc.xml.interceptor;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

import edu.gcc.xml.Schema;
import edu.gcc.xml.annotation.XmlUpdate;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

public class UpdateInterceptor<T> {
	private Schema<T> schema;
	
	public UpdateInterceptor(final Schema<T> schema) {
		this.schema = schema;
	}
	
	@RuntimeType
	public Object update(@Origin Method method, @Argument(0) final T object) {
		if(method.getAnnotation(XmlUpdate.class).async())
			return CompletableFuture.runAsync(() -> schema.update(object));
		
		return schema.update(object);
	}
}
