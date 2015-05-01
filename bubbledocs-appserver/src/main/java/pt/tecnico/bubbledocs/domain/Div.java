package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exceptions.NullContentException;

/**
 * @author pc-w
 *
 */
public class Div extends Div_Base {
    
    /**
     * 
     */
    public Div() {
        super();
    }
	/**
	 * @param arg1
	 * @param arg2
	 */
	public Div(FunctionArgument arg1, FunctionArgument arg2){
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
		
		return val1 / val2;
	}

	/* (non-Javadoc)
	 * @see pt.tecnico.bubbledocs.dml.Function#exportToXML()
	 */
	@Override
	public Element exportToXML() {
		Element element = new Element("div");
    	element.addContent(this.getArg1().exportToXML());
    	element.addContent(this.getArg2().exportToXML());
	return element;
	}
	
	@Override
	public String getName() {
		return "div";
	}

	
}
