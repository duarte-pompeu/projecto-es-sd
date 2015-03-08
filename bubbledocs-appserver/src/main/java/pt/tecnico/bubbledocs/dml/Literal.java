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
		this.setValue(value);
	}
	
	public int getIntValue(){
		return this.getValue().intValue();
	}

    public void importFromXML(Element literalElement) {
    	int value = Integer.parseInt(literalElement.getAttribute("value").getValue());
    	init(value);
    }

    public Element exportToXML() {
    	Element element = new Element("literal");
    	element.setAttribute("value", ""+this.getValue());
	return element;
    }
    
    @Override
	public String toString(){
		return Integer.toString(this.getValue());
	}
}
