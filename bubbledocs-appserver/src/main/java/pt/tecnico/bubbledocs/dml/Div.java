package pt.tecnico.bubbledocs.dml;

import java.util.List;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exceptions.NullContentException;

public class Div extends Div_Base {
    
    public Div() {
        super();
    }
	public Div(FunctionArgument arg1, FunctionArgument arg2){
		super();
		super.init(arg1, arg2);
	}
	
	@Override
	public int getValue() throws NullContentException {
		int val1 = this.getArg1().getValue();
		int val2 = this.getArg2().getValue();
		
		return val1 / val2;
	}

	@Override
	public Element exportToXML() {
		Element element = new Element("div");
    	element.addContent(this.getArg1().exportToXML());
    	element.addContent(this.getArg2().exportToXML());
	return element;
	}

	
}
