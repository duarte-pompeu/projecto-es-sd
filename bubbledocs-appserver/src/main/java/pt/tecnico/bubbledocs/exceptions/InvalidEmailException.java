package pt.tecnico.bubbledocs.exceptions;

public class InvalidEmailException extends BubbleDocsException {

	private static final long serialVersionUID = -3543607944092143442L;
	
	public InvalidEmailException(){
		super();
	}
	
	public InvalidEmailException(String message){
		super(message);
	}

}
