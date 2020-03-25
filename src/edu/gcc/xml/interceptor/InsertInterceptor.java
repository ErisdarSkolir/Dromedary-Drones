package edu.gcc.xml.interceptor;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

import edu.gcc.xml.Schema;
import edu.gcc.xml.annotation.XmlInsert;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

public class InsertInterceptor<T> {
	private Schema<T> schema;
	
	public InsertInterceptor(final Schema<T> schema) {
		this.schema = schema;
	}
	
	@RuntimeType
	public Object insert(@Origin Method method, @Argument(0) final T object) {
		if(method.getAnnotation(XmlInsert.class).async())
			return CompletableFuture.runAsync(()-> schema.insert(object));
		
		schema.insert(object);
		
		return Void.TYPE;
	}
}
