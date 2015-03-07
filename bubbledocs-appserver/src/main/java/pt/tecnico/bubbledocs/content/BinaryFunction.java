package pt.tecnico.bubbledocs.content;

public abstract class BinaryFunction extends Function {
	private Content arg1, arg2;
	
	public BinaryFunction(){
		
	}
	
	public BinaryFunction(Content arg1, Content arg2){
		init(arg1, arg2);
	}
	
	private void init(Content arg1, Content arg2){
		this.arg1 = arg1;
		this.arg2 = arg2;
	}
	
	public Content getArg1(){	return this.arg1; }
	public Content getArg2(){	return this.arg2; }
}
