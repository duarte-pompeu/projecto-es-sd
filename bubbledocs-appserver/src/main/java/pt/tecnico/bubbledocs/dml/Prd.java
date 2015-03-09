package pt.tecnico.bubbledocs.dml;

import java.util.List;

import org.jdom2.Element;



import pt.tecnico.bubbledocs.exceptions.NullContentException;

public class Prd extends Prd_Base {
    
    public Prd() {
        super();
    }
    
    public Prd(Range range){
		super.init(range);
	}
	
	@Override
	public int getValue() throws NullContentException {
		// initialize with 1 since we're multiplying
		int total = 1;
		
		for(Cell cell: this.getRangeCells()){
			total *= cell.getContent().getValue();
		}
		
		return total;
		
	}

	@Override
	public Element exportToXML() {
		Element element = new Element("prd");
    	element.addContent(this.getRange().exportToXML());
	return element;
	}

	@Override
	public void importFromXML(Element element) {
		List<Element> rangeElement = element.getChildren();
    	Element c=rangeElement.get(0);
    	Range r=new Range();
    	r.importFromXML(c);
    	this.setRange(r);
	}

    
}
