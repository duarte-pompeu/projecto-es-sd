package pt.tecnico.bubbledocs.exceptions;

/**
 * 
 * @author danix
 *
 * This exception is used when logging in fails, whether by wrong username or password. 
 */
public class LoginException extends BubbleDocsException {

	private static final long serialVersionUID = -8574314709032088218L;

	public LoginException() {
		super();
	}

	public LoginException(String message) {
		super(message);
	}

	public LoginException(Throwable cause) {
		super(cause);
	}

	public LoginException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoginException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
