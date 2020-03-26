package edu.gcc.xml.exception;

public class XmlSchemaCreationException extends RuntimeException {
	private static final long serialVersionUID = -7969901499263843583L;

	public XmlSchemaCreationException(final String message) {
		super(message);
	}
	
	public XmlSchemaCreationException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
