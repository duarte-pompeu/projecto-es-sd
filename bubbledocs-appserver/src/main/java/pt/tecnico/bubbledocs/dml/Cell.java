package pt.tecnico.bubbledocs.dml;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.content.Content;
import pt.tecnico.bubbledocs.exceptions.NullContentException;

public class Cell extends Cell_Base {
    private Content content;
    
    public Cell(int column, int line) {
    	super();
    	this.setLine(new Integer(line)); 
        this.setColumn(new Integer(column));
    }
    
    public Cell() {
		// TODO Auto-generated constructor stub
	}

	public Content getContent() throws NullContentException{
		if(content == null){
			throw new NullContentException(getLine(), getColumn());
		}
		
		return this.content;
	}
    

    
    public void importFromXML(Element cellElement) {
    	this.setLine(new Integer(cellElement.getAttribute("line").getValue()));
    	this.setColumn(new Integer(cellElement.getAttribute("column").getValue()));
    	
    	//now what?
    }

    public Element exportToXML() {
    	Element element = new Element("cell");
    	element.setAttribute("line", this.getLine().toString());
    	element.setAttribute("column", this.getColumn().toString());
    	this.content.exportToXML();
	
	return element;
    }
}
