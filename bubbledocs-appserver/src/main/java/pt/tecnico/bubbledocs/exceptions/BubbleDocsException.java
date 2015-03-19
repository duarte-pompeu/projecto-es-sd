package pt.tecnico.bubbledocs.exceptions;

public class BubbleDocsException extends RuntimeException {

	private static final long serialVersionUID = -3443130309722963275L;

	public BubbleDocsException() {
		super();
	}

	public BubbleDocsException(String message) {
		super(message);
	}

	public BubbleDocsException(Throwable cause) {
		super(cause);
	}

	public BubbleDocsException(String message, Throwable cause) {
		super(message, cause);
	}

	public BubbleDocsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
