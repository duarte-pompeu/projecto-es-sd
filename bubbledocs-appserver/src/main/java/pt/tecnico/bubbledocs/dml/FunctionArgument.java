package pt.tecnico.bubbledocs.dml;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exceptions.NullContentException;

public abstract class FunctionArgument extends FunctionArgument_Base {
    
    public FunctionArgument() {
        super();
    }
    

	@Override
	public abstract Element exportToXML();

	@Override
	public abstract int getValue() throws NullContentException;

	@Override
	public abstract void importFromXML(Element c);
    
}
