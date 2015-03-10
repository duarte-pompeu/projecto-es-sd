package pt.tecnico.bubbledocs.dml;

import java.util.List;

import org.jdom2.Element;


public class RangeFunction extends RangeFunction_Base {
    
    public RangeFunction() {
        super();
    }
    public RangeFunction(Range range){
		init(range);
	}
	
	protected void init(Range range){
		this.setRange(range);
	}
	
	public Iterable<Cell> getRangeCells(){
		return this.getRange().getIterable();
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
