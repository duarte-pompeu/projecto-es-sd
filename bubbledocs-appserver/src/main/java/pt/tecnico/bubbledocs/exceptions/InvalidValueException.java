package pt.tecnico.bubbledocs.exceptions;

/*
 * This exception is thrown when an user creates an object 
 * like a calcsheet with the wrong number 
 * of rows and columns;
 */

public class InvalidValueException extends BubbleDocsException {

	private static final long serialVersionUID = 476115369516210901L;

	public InvalidValueException() {
	}

	public InvalidValueException(String message) {
		super(message);
	}

	public InvalidValueException(Throwable cause) {
		super(cause);
	}

	public InvalidValueException(String message, Throwable cause) {
		super(message, cause);
	}
}

