package pt.tecnico.bubbledocs.domain;

import java.util.List;

import org.jdom2.Element;




/**
 * @author pc-w
 *
 */
public abstract class RangeFunction extends RangeFunction_Base {
    
    /**
     * 
     */
    public RangeFunction() {
        super();
    }
    /**
     * @param range
     */
    public RangeFunction(Range range){
		init(range);
	}
	
	/**
	 * @param range
	 */
	protected void init(Range range){
		this.setRange(range);
	}
	
	/**
	 * @return
	 */
	public Iterable<Cell> getRangeCells(){
		return this.getRange();
	}
	
	public void accept(CalcSheetExporter exporter) {
		exporter.exportRangeFunction(this);
	}
	
	/* (non-Javadoc)
	 * @see pt.tecnico.bubbledocs.dml.Function#importFromXML(org.jdom2.Element)
	 */
	@Override
	public void importFromXML(Element element) {
		
		List<Element> rangeElement = element.getChildren();
    	Element c=rangeElement.get(0);
    	Range r=new Range();
    	r.importFromXML(c);
    	this.setRange(r);
	}

}
