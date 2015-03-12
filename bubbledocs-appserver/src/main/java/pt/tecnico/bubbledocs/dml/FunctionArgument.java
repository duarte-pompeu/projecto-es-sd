package pt.tecnico.bubbledocs.dml;

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
	 * @see pt.tecnico.bubbledocs.dml.Content#exportToXML()
	 */
	@Override
	public abstract Element exportToXML();

	/* (non-Javadoc)
	 * @see pt.tecnico.bubbledocs.dml.Content#getValue()
	 */
	@Override
	public abstract int getValue() throws NullContentException;

	/* (non-Javadoc)
	 * @see pt.tecnico.bubbledocs.dml.Content#importFromXML(org.jdom2.Element)
	 */
	@Override
	public abstract void importFromXML(Element c);
    
}
