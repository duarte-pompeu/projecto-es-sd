package pt.tecnico.bubbledocs.exceptions;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6648681915942771821L;

	public NotFoundException() {
		super();
	}

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(Throwable cause) {
		super(cause);
	}

	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
