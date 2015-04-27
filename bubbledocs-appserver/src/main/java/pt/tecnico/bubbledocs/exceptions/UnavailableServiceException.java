package pt.tecnico.bubbledocs.exceptions;

public class UnavailableServiceException extends BubbleDocsException {

	private static final long serialVersionUID = 198952681247438212L;
	
	public UnavailableServiceException(){
		super();
	}
	
	public UnavailableServiceException(String message){
		super(message);
	}
	
	public UnavailableServiceException(Throwable t) {
		super(t);
	}
}
