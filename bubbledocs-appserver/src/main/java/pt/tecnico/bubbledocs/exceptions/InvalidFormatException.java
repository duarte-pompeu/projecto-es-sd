package pt.tecnico.bubbledocs.exceptions;

public class InvalidFormatException extends BubbleDocsException {
	private static final long serialVersionUID = -7649896740276967386L;

	public InvalidFormatException(){
		super();
	}
	
	public InvalidFormatException(String message){
		super(message);
	}
}
