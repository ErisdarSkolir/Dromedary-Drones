package edu.gcc.xml.interceptor;

import edu.gcc.xml.Schema;
import net.bytebuddy.asm.Advice.Argument;

public class UpdateInterceptor<T> {
	private Schema<T> schema;
	
	public UpdateInterceptor(final Schema<T> schema) {
		this.schema = schema;
	}
	
	public boolean update(@Argument(0) final T object) {
		return schema.update(object);
	}
}
