package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;


import pt.tecnico.bubbledocs.exceptions.NullContentException;

/**
 * @author pc-w
 *
 */
public abstract class Function extends Function_Base {
    
    /**
     * 
     */
    public Function() {
        super();
    }

	/* (non-Javadoc)
	 * @see pt.tecnico.bubbledocs.dml.Content#exportToXML()
	 */
	@Override
	public Element exportToXML() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see pt.tecnico.bubbledocs.dml.Content#getValue()
	 */
	@Override
	public int getValue() throws NullContentException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see pt.tecnico.bubbledocs.dml.Content#importFromXML(org.jdom2.Element)
	 */
	@Override
	public void importFromXML(Element c) {
		// TODO Auto-generated method stub
		
	}
	
	public abstract String getName();
    
}
