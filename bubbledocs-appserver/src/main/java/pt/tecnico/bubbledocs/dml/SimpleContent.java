package pt.tecnico.bubbledocs.dml;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exceptions.NullContentException;

public abstract class SimpleContent extends SimpleContent_Base {
    
    public SimpleContent() {
        super();
    }

	@Override
	public abstract Element exportToXML();

	@Override
	public abstract int getValue() throws NullContentException;

	@Override
	public abstract void importFromXML(Element c);
    
}
