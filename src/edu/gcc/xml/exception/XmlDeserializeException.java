package edu.gcc.xml.exception;

public class XmlDeserializeException extends RuntimeException{
	private static final long serialVersionUID = -7342295503893512725L;

	public XmlDeserializeException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
