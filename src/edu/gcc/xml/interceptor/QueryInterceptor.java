package edu.gcc.xml.interceptor;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import edu.gcc.xml.Schema;
import edu.gcc.xml.annotation.XPathQuery;
import net.bytebuddy.asm.Advice.AllArguments;
import net.bytebuddy.asm.Advice.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

public class QueryInterceptor<T> {
	private Schema<T> schema;
	
	public QueryInterceptor(final Schema<T> schema) {
		this.schema = schema;
	}
	
	@RuntimeType
	public T get(@Origin Method method, @AllArguments Object... args) {
		String xPath = method.getAnnotation(XPathQuery.class).value();
		
		for(int i = 0; i < args.length; i++) {
			xPath = xPath.replaceAll(String.format("(\\{%d\\})", i), args[i].toString());
		}
		
		return schema.get(xPath);
	}
	
	/*public CompletableFuture<T> getAsyn(@Origin Method method, @AllArguments Object... args){
		return CompletableFuture.supplyAsync(() -> get(method, args));
	}*/
}
