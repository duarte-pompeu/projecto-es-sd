package pt.tecnico.bubbledocs.exceptions;

public class NullContentException extends Exception{
	// The serializable class NullContentException does not declare a 
	// static final serialVersionUID field of type long
	// from superclass Exception
	private static final long serialVersionUID = 1L;
	
	private final int column, line;
	
	public NullContentException(int line, int column){
		this.column = column;
		this.line = line;
	}
	
	@Override
	public String getMessage(){	
		return "NullContentException: Cell "
			+ line + ";" + column + " is empty.";
	}
}
