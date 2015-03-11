package pt.tecnico.bubbledocs.dml;

import java.util.List;

import org.jdom2.Element;
import pt.tecnico.bubbledocs.exceptions.NullContentException;

public class Cell extends Cell_Base {
    
	public Cell() {
		super();
	}
	
	public Cell(int line, int column, Content content) {
    	super();
    	init(line, column, content);
    }
	
	public Cell(int line, int column) {
		super();
		init(line, column, null);
	}
	
	public void init(int line, int column, Content content){
		this.setLine(new Integer(line)); 
        this.setColumn(new Integer(column));
        this.setId(new Integer(line*column+line));
        if(content!=null){
        	this.setContent(content);
        	this.setContentRepresentation(content.toString());
        }
	}
    
	public Content _getContent() throws NullContentException{
	
		if(this.getContent() == null){
			throw new NullContentException(getLine(), getColumn());
		}
		
		return this.getContent();
	}
	
	@Override
	public void setContent(Content c){
		super.setContent(c);
		setContentRepresentation(c.toString());
	}
    
    public void importFromXML(Element cellElement) {
    	this.setLine(new Integer(cellElement.getAttribute("line").getValue()));
    	this.setColumn(new Integer(cellElement.getAttribute("column").getValue()));
    	
    	List<Element> contentElement = cellElement.getChildren();
    	Element c=contentElement.get(0);
    	String name=c.getName();
    		
    	Content content=BubbleDocs.parseName(name);
    	try{
    		content.importFromXML(c);
    	}catch(NullPointerException e){ System.out.println(String.format("Unknown content type %s", name));}
    	
    	this.setContent(content);
    }

    public Element exportToXML() {
    	Element element = new Element("cell");
    	element.setAttribute("line", this.getLine().toString());
    	element.setAttribute("column", this.getColumn().toString());
    	if(this.getContent()!=null)
    		element.addContent(this.getContent().exportToXML());
    	
	return element;
    }
}
