package pt.tecnico.bubbledocs.content;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exceptions.NullContentException;

public abstract class Content {
	
	public abstract int getValue() throws NullContentException;
	
	public abstract Element exportToXML();
	public abstract void importFromXML(Element element);
}
