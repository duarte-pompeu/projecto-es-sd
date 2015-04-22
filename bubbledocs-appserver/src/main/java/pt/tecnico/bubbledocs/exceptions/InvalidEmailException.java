package pt.tecnico.bubbledocs.exceptions;


public class InvalidEmailException extends BubbleDocsException {

	public InvalidEmailException() {
		super();
	}
	
	public InvalidEmailException(Throwable e) {
		super(e);
	}

	private static final long serialVersionUID = -3543607944092143442L;

}
