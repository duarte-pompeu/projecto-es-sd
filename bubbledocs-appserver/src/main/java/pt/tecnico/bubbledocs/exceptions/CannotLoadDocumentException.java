package pt.tecnico.bubbledocs.exceptions;

public class CannotLoadDocumentException extends BubbleDocsException {
	private static final long serialVersionUID = 5659402603842701056L;
	
	public CannotLoadDocumentException() {
		super();
	}
	
	
	public CannotLoadDocumentException(String message){
		super(message);
	}
}
