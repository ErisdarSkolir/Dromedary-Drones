package edu.gcc.xml.exception;

public class XmlSerializationException extends RuntimeException{
	private static final long serialVersionUID = 6672462038604176097L;

	public XmlSerializationException(final String message) {
		super(message);
	}
	
	public XmlSerializationException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
