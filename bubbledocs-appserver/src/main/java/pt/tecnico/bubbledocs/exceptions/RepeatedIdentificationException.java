package pt.tecnico.bubbledocs.exceptions;

/*
 * This exception is used when an object with unique identification is added
 * (Like User and CalcSheet in this project)
 */

public class RepeatedIdentificationException extends BubbleDocsException {

	private static final long serialVersionUID = -3487585108085182671L;

	public RepeatedIdentificationException() {
		super();
	}

	public RepeatedIdentificationException(String message) {
		super(message);
	}

	public RepeatedIdentificationException(Throwable cause) {
		super(cause);
	}

	public RepeatedIdentificationException(String message, Throwable cause) {
		super(message, cause);
	}

}
