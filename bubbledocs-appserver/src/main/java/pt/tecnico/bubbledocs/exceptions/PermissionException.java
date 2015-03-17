package pt.tecnico.bubbledocs.exceptions;

/*
 * This exception is thrown when an action that is done without permission
 */

public class PermissionException extends RuntimeException {

	private static final long serialVersionUID = 476115369516210901L;

	public PermissionException() {
	}

	public PermissionException(String message) {
		super(message);
	}

	public PermissionException(Throwable cause) {
		super(cause);
	}

	public PermissionException(String message, Throwable cause) {
		super(message, cause);
	}
}
