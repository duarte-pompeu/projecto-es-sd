package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exceptions.InvalidFormatException;



/**
 * @author pc-w
 *
 */
public class Literal extends Literal_Base {
    
    /**
     * 
     */
    public Literal() {
        super();
    }

    /**
     * @param value
     */
    public Literal(int value){
		super();
    	init(value);
	}
    
    public Literal(String literal) throws InvalidFormatException {
    	super();
    	int literal_val;
    	try{
    		literal_val = Integer.valueOf(literal);
    	}
    	catch (NumberFormatException NFEexception){
    		throw new InvalidFormatException(literal + " isnt an integer.");
    	}
    	init(literal_val);
    }
	
	/**
	 * @param value
	 */
	private void init(int value){
		this.setVal(value);
	}
	
	/* (non-Javadoc)
	 * @see pt.tecnico.bubbledocs.dml.FunctionArgument#getValue()
	 */
	public int getValue(){
		return this.getVal();
	}
	
	public void accept(CalcSheetExporter exporter) {
		exporter.exportLiteral(this);
	}
	
    /* (non-Javadoc)
     * @see pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject#toString()
     */
    @Override
	public String toString(){
		return Integer.toString(this.getValue());
	}
}
