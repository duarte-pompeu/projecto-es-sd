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
	 * @see pt.tecnico.bubbledocs.dml.Content#getValue()
	 */
	@Override
	public int getValue() throws NullContentException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public abstract String getName();
    
}
