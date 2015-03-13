package pt.tecnico.bubbledocs.dml;

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

	/* (non-Javadoc)
	 * @see pt.tecnico.bubbledocs.dml.Function#exportToXML()
	 */
	@Override
	public Element exportToXML() {
		Element element = new Element("mull");
    	element.addContent(this.getArg1().exportToXML());
    	element.addContent(this.getArg2().exportToXML());
	return element;
	}
}
