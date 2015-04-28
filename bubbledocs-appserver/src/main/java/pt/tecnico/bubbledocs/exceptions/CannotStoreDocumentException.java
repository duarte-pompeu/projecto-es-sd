package pt.tecnico.bubbledocs.exceptions;

public class CannotStoreDocumentException extends BubbleDocsException {
	private static final long serialVersionUID = -7987882624554957525L;
	
	public CannotStoreDocumentException() {
		super();
	}
	
	
	public CannotStoreDocumentException(String message){
		super(message);
	}
}
