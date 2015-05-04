package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exceptions.NullContentException;

/**
 * @author pc-w
 *
 */
public class Mul extends Mul_Base {
    
    /**
     * 
     */
    public Mul() {
        super();
    }
    
    /**
     * @param arg1
     * @param arg2
     */
    public Mul(FunctionArgument arg1, FunctionArgument arg2){
		super();
    	super.init(arg1, arg2);
	}
	
	/* (non-Javadoc)
	 * @see pt.tecnico.bubbledocs.dml.Function#getValue()
	 */
	@Override
	public int getValue() throws NullContentException {
		int val1 = getArg1().getValue();
		int val2 = getArg2().getValue();
		
		return val1 * val2;
	}

	@Override
	public String getName() {
		return "mul";
	}
}
