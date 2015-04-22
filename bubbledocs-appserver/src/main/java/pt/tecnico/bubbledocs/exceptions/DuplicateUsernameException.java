package pt.tecnico.bubbledocs.exceptions;


public class DuplicateUsernameException extends BubbleDocsException {

	private static final long serialVersionUID = -5864091120608617698L;
	
	public DuplicateUsernameException() {
		super();
	}
	
	public DuplicateUsernameException(Throwable e) {
		super(e);
	}


}
