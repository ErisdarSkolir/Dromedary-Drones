package edu.gcc.xml.interceptor;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

import edu.gcc.xml.Schema;
import edu.gcc.xml.annotation.XPathQuery;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

public class QueryInterceptor<T> {
	private Schema<T> schema;
	
	public QueryInterceptor(final Schema<T> schema) {
		this.schema = schema;
	}
	
	@RuntimeType
	public Object get(@Origin Method method, @AllArguments Object[] args) {
		XPathQuery annotation = method.getAnnotation(XPathQuery.class);
		
		if(annotation.asynchronous()) {
			if(annotation.list()) 
				return CompletableFuture.supplyAsync(() -> schema.getList(replaceArguments(annotation.value(), args)));
			
			return CompletableFuture.supplyAsync(() -> schema.get(replaceArguments(annotation.value(), args)));
		}
		
		if(annotation.list())
			return schema.getList(replaceArguments(annotation.value(), args));
		
		return schema.get(replaceArguments(annotation.value(), args));
	}
	
	private String replaceArguments(final String xPath, final Object[] args) {
		String query = xPath;
		
		for(int i = 0; i < args.length; i++) {
			query = query.replaceAll(String.format("(\\{%d\\})", i), args[i].toString());
		}
		
		return query;
	}
}
