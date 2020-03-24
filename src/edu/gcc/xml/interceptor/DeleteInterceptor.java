package edu.gcc.xml.interceptor;

import edu.gcc.xml.Schema;
import net.bytebuddy.asm.Advice.Argument;

public class DeleteInterceptor<T> {
	private Schema<T> schema;
	
	public DeleteInterceptor(final Schema<T> schema) {
		this.schema = schema;
	}
	
	public boolean delete(@Argument(0) final T object) {
		return schema.delete(object);
	}
}
