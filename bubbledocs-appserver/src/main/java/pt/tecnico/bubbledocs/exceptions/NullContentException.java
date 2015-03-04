package pt.tecnico.bubbledocs.exceptions;

public class NullContentException extends Exception{
	private final int column, line;
	public NullContentException(int column, int line){
		this.column = column;
		this.line = line;
	}
	
	@Override
	public String getMessage(){
		
	return "NullContentException: Cell "
			+ column + ";" + line + " is empty.";
	}
}
