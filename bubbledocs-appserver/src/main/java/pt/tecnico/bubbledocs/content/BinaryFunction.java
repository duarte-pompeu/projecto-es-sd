package pt.tecnico.bubbledocs.content;

public abstract class BinaryFunction extends Function {
	private Content arg1, arg2;
	
	public BinaryFunction(){
		
	}
	
	public BinaryFunction(Content arg1, Content arg2){
		setArgs(arg1, arg2);
	}
	
	public void setArgs(Content arg1, Content arg2){
		this.arg1 = arg1;
		this.arg2 = arg2;
	}
}
