package pt.tecnico.bubbledocs.domain;

import pt.tecnico.bubbledocs.exceptions.NullContentException;

/**
 * @author pc-w
 *
 */
public class Add extends Add_Base {
    
    /**
     * 
     */
    public Add() {
        super();
    }
    /**
     * @param arg1
     * @param arg2
     */
    public Add(FunctionArgument arg1, FunctionArgument arg2){
		super();
    	super.init(arg1, arg2);
	}
	
	/* (non-Javadoc)
	 * @see pt.tecnico.bubbledocs.dml.Function#getValue()
	 */
	@Override
	public int getValue() throws NullContentException {
		int val1 = this.getArg1().getValue();
		int val2 = this.getArg2().getValue();
		
		return val1 + val2;
	}

	
	@Override
	public String getName() {
		return "add";
	}

}
