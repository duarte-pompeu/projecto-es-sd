package pt.tecnico.bubbledocs.dml;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exceptions.NullContentException;

public abstract class Content extends Content_Base {
    
    public Content() {
        super();
    }

	public abstract Element exportToXML();
	

	public abstract int getValue() throws NullContentException;
	public abstract void importFromXML(Element c);
    
}
