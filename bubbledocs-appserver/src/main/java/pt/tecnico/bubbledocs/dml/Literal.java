package pt.tecnico.bubbledocs.dml;

import org.jdom2.Element;

public class Literal extends Literal_Base {
    
    public Literal() {
        super();
    }

    public Literal(int value){
		init(value);
	}
	
	private void init(int value){
		this.setVal(value);
	}
	
	public int getValue(){
		return this.getVal().intValue();
	}

    public void importFromXML(Element literalElement) {
    	int value = Integer.parseInt(literalElement.getAttribute("val").getValue());
    	init(value);
    }

    public Element exportToXML() {
    	Element element = new Element("literal");
    	element.setAttribute("val", ""+this.getValue());
	return element;
    }
    
    @Override
	public String toString(){
		return Integer.toString(this.getValue());
	}
}
