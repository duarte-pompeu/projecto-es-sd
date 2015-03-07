package pt.tecnico.bubbledocs.content;
import org.jdom2.Element;


public class Literal extends SimpleContent {

	private int value;
	
	public Literal(){
	}
	
	public Literal(int value){
		init(value);
	}
	
	private void init(int value){
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
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
		return Integer.toString(value);
	}
}
