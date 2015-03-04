package pt.tecnico.bubbledocs.content;

import pt.tecnico.bubbledocs.exceptions.NullContentException;

public abstract class Content {
	
	public abstract int getValue() throws NullContentException;
}
