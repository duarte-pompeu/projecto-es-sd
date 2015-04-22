package pt.tecnico.bubbledocs.exceptions;


public class DuplicateEmailException extends BubbleDocsException {

	public DuplicateEmailException() {
		super();
	}
	
	public DuplicateEmailException(Throwable e) {
		super(e);
	}

	private static final long serialVersionUID = 7997154706196984160L;

}
