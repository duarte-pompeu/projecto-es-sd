package pt.tecnico.bubbledocs.exceptions;


public class InvalidUsernameException extends BubbleDocsException {

	private static final long serialVersionUID = 7812621860749121799L;
	
	public InvalidUsernameException(){
		super();
	}
	
	public InvalidUsernameException(String message){
		super(message);
	}

	public InvalidUsernameException(Throwable e) {
		super(e);
	}

}
