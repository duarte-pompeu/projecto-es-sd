package pt.tecnico.bubbledocs.dml;

import java.util.List;

import org.jdom2.Element;




import pt.tecnico.bubbledocs.exceptions.NullContentException;

public class Cell extends Cell_Base {
    
    public Cell(int column, int line) {
    	super();
    	this.setLine(new Integer(line)); 
        this.setColumn(new Integer(column));
        this.setId(new Integer(line*column+line));
    }
    
    public Cell() {
		// TODO Auto-generated constructor stub
	}

    
	public Content _getContent() throws NullContentException{
	
		if(this.getContent() == null){
			throw new NullContentException(getLine(), getColumn());
		}
		
		return this.getContent();
	}
    

    
    public void importFromXML(Element cellElement) {
    	this.setLine(new Integer(cellElement.getAttribute("line").getValue()));
    	this.setColumn(new Integer(cellElement.getAttribute("column").getValue()));
    	
    	List<Element> contentElement = cellElement.getChildren();
    	Element c=contentElement.get(0);
    	String name=c.getName();
    		
    	Content content=parseName(name);
    	try{
    		content.importFromXML(c);
    	}catch(NullPointerException e){ System.out.println(String.format("Unknown content type %s", name));}
    	
    }

    public Element exportToXML() {
    	Element element = new Element("cell");
    	element.setAttribute("line", this.getLine().toString());
    	element.setAttribute("column", this.getColumn().toString());
    	
    	this.getContent().exportToXML();
    	
	return element;
    }
    
    
    private Content parseName(String name){
    	switch(name){
    	case "literal": return new Literal();
    	case "reference": return new Reference();
    	case "add": return new Add();
    	case "sub": return new Sub();
    	case "mul": return new Mul();
    	case "div": return new Div();
    	case "avg": return new Avg();
    	case "prd": return new Prd();
    	default: return null;
    	}	
    }
}
