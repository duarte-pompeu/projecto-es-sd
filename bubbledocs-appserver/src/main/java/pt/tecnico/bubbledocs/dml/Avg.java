package pt.tecnico.bubbledocs.dml;

import java.util.List;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exceptions.NullContentException;

public class Avg extends Avg_Base {
    
    public Avg() {
        super();
    }
    
    public Avg(Range range){
		super.init(range);
	}
	
	@Override
	public int getValue() throws NullContentException {
		int total = 0;
		int n = 0;
		
		for(Cell cell: this.getRangeCells()){
			total += cell.getContent().getValue();
			n += 1;
		}
		
		return total / n;
	}

	@Override
	public Element exportToXML() {
		Element element = new Element("avg");
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
