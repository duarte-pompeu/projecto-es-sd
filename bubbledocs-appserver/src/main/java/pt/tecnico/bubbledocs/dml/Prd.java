package pt.tecnico.bubbledocs.dml;

import java.util.List;

import org.jdom2.Element;



import pt.tecnico.bubbledocs.exceptions.NullContentException;

/**
 * @author pc-w
 *
 */
public class Prd extends Prd_Base {
    
    /**
     * 
     */
    public Prd() {
        super();
    }
    
    /**
     * @param range
     */
    public Prd(Range range){
		super();
    	super.init(range);
	}
	
	/* (non-Javadoc)
	 * @see pt.tecnico.bubbledocs.dml.Function#getValue()
	 */
	@Override
	public int getValue() throws NullContentException {
		// initialize with 1 since we're multiplying
		int total = 1;
		
		for(Cell cell: this.getRangeCells()){
			total *= cell.getContent().getValue();
		}
		
		return total;
		
	}

	/* (non-Javadoc)
	 * @see pt.tecnico.bubbledocs.dml.Function#exportToXML()
	 */
	@Override
	public Element exportToXML() {
		Element element = new Element("prd");
    	element.addContent(this.getRange().exportToXML());
	return element;
	}

	
}
