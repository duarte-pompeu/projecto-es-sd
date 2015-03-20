package pt.tecnico.bubbledocs.exceptions;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;


/**
 * When a service is executed but the provided token indicates either a non existing
 * Session or an expired one.
 * @author 
 *
 */
public class UserNotInSessionException extends BubbleDocsException {

	private static final long serialVersionUID = 6914324876972376410L;

	public UserNotInSessionException() {
		super();
	}

	public UserNotInSessionException(String message) {
		super(message);
	}

	public UserNotInSessionException(Throwable cause) {
		super(cause);
	}

	public UserNotInSessionException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserNotInSessionException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
