package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;


import pt.tecnico.bubbledocs.exceptions.NullContentException;

/**
 * @author pc-w
 *
 */
public abstract class FunctionArgument extends FunctionArgument_Base {
    
    /**
     * 
     */
    public FunctionArgument() {
        super();
    }

	/* (non-Javadoc)
	 * @see pt.tecnico.bubbledocs.dml.Content#getValue()
	 */

	public abstract int getValue() throws NullContentException;

	/* (non-Javadoc)
	 * @see pt.tecnico.bubbledocs.dml.Content#importFromXML(org.jdom2.Element)
	 */

	public abstract void importFromXML(Element c);
	
	public abstract void accept(CalcSheetExporter exporter);
    
}
