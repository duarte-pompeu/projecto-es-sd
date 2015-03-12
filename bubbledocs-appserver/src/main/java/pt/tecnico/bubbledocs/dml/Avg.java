package pt.tecnico.bubbledocs.dml;

import java.util.List;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exceptions.NullContentException;

/**
 * @author pc-w
 *
 */
public class Avg extends Avg_Base {
    
    /**
     * 
     */
    public Avg() {
        super();
    }
    
    /**
     * @param range
     */
    public Avg(Range range){
		super();
    	super.init(range);
	}
	
	/* (non-Javadoc)
	 * @see pt.tecnico.bubbledocs.dml.Function#getValue()
	 */
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

	/* (non-Javadoc)
	 * @see pt.tecnico.bubbledocs.dml.Function#exportToXML()
	 */
	@Override
	public Element exportToXML() {
		Element element = new Element("avg");
    	element.addContent(this.getRange().exportToXML());
	return element;
	}

	
}
