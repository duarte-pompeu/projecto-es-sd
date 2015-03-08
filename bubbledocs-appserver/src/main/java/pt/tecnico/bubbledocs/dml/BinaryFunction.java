package pt.tecnico.bubbledocs.dml;

public class BinaryFunction extends BinaryFunction_Base {
    
    public BinaryFunction() {
        super();
    }
    
    public BinaryFunction(SimpleContent arg1, SimpleContent arg2){
		init(arg1, arg2);
	}
	
	protected void init(SimpleContent arg1, SimpleContent arg2){
		this.getArgumentsSet().add(arg1);
		this.getArgumentsSet().add(arg2);
	}
	
	public SimpleContent getArg1(){	return this.getArgumentsSet().iterator().next(); }
	public SimpleContent getArg2(){	
		this.getArgumentsSet().iterator().next(); 
		return this.getArgumentsSet().iterator().next();
	}
    
}
