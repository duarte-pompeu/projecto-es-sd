package pt.tecnico.bubbledocs.dml;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exceptions.NullContentException;

public class Mul extends Mul_Base {
    
    public Mul() {
        super();
    }
    
    public Mul(SimpleContent arg1, SimpleContent arg2){
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
		Element element = new Element("mul");
    	element.addContent(this.getArg1().exportToXML());
    	element.addContent(this.getArg2().exportToXML());
	return element;
	}

	@Override
	public void importFromXML(Element element) {
		// TODO Auto-generated method stub

	}
    
}
