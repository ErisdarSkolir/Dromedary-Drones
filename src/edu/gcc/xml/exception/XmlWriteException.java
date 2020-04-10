package edu.gcc.xml.exception;

public class XmlWriteException extends RuntimeException {
	private static final long serialVersionUID = 1852323305667094550L;

	public XmlWriteException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
