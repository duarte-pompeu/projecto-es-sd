package pt.tecnico.bubbledocs.dml;

import java.util.List;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exceptions.NullContentException;

public class Mul extends Mul_Base {
    
    public Mul() {
        super();
    }
    
    public Mul(FunctionArgument arg1, FunctionArgument arg2){
		super();
    	super.init(arg1, arg2);
	}
	
	@Override
	public int getValue() throws NullContentException {
		int val1 = getArg1().getValue();
		int val2 = getArg2().getValue();
		
		return val1 * val2;
	}

	@Override
	public Element exportToXML() {
		Element element = new Element("mull");
    	element.addContent(this.getArg1().exportToXML());
    	element.addContent(this.getArg2().exportToXML());
	return element;
	}
}
