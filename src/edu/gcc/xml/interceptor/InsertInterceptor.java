package edu.gcc.xml.interceptor;

import edu.gcc.xml.Schema;
import net.bytebuddy.asm.Advice.Argument;

public class InsertInterceptor<T> {
	private Schema<T> schema;
	
	public InsertInterceptor(final Schema<T> schema) {
		this.schema = schema;
	}
	
	public void insert(@Argument(0) final T object) {
		schema.insert(object);
	}
}
