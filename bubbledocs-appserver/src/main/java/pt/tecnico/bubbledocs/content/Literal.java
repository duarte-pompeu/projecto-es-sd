package pt.tecnico.bubbledocs.content;
import org.jdom2.Element;


public class Literal extends SimpleContent {

	private int value;
	
	public Literal(int value){
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public void setValue(int value){
		this.value=value;
	}
	
	@Override
	public String toString(){
		return Integer.toString(value);
	}
	

    public void importFromXML(Element literalElement) {
    	this.setValue(Integer.parseInt(literalElement.getAttribute("value").getValue()));
    }

    public Element exportToXML() {
    	Element element = new Element("literal");
    	element.setAttribute("value", ""+this.getValue());
	return element;
    }


}
