package pt.tecnico.bubbledocs.domain;

import java.util.List;

import org.jdom2.Element;


import pt.tecnico.bubbledocs.exceptions.NullContentException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;

/**
 * @author pc-w
 *
 */
public class Cell extends Cell_Base {
    
	/**
	 * 
	 */
	public Cell() {
		super();
	}
	
	/**
	 * @param line
	 * @param column
	 * @param content
	 */
	public Cell(int line, int column, Content content) {
    	super();
    	init(line, column, content);
    }
	
	/**
	 * @param line
	 * @param column
	 */
	public Cell(int line, int column) {
		super();
		init(line, column, null);
	}
	
	/**
	 * @param line
	 * @param column
	 * @param content
	 */
	public void init(int line, int column, Content content){
		this.setProtect(false);
		this.setLine(new Integer(line)); 
        this.setColumn(new Integer(column));
        String newId = String.valueOf(line) + ";" +	
        			String.valueOf(column);
        this.setId(newId);
   
	}
	
	/* (non-Javadoc)
	 * @see pt.tecnico.bubbledocs.dml.Cell_Base#setContent(pt.tecnico.bubbledocs.dml.Content)
	 */
	@Override
	public void setContent(Content c) throws PermissionException{
		if(this.getProtect()){
			throw new PermissionException("Cell " + getId() + " is protected.");
		}
		
		super.setContent(c);
		
	}
	
	  public void delete() {
		  if(this.getContent()!=null)
		    	this.getContent().delete();
		  for (Reference ref : this.getReferenceSet() ) {
	    		ref.delete();
	    		}
		  for (ReferenceArgument ref : this.getReferenceArgumentSet() ) {
	    		ref.delete();
	    		}
	    	this.setLine(null);
	    	this.setColumn(null);
	    	setCalcSheet(null);
	    	deleteDomainObject();
	        }
    
    /**
     * @param cellElement
     */
    public void importFromXML(Element cellElement) {
    	this.setId(new String(cellElement.getAttribute("id").getValue()));
    	this.setLine(new Integer(cellElement.getAttribute("line").getValue()));
    	this.setColumn(new Integer(cellElement.getAttribute("column").getValue()));
    	this.setProtect(new Boolean(cellElement.getAttribute("protect").getValue()));
    	
    	List<Element> contentElement = cellElement.getChildren();
    	
    	if(contentElement.isEmpty())
    		return;
    	
    	Element c=contentElement.get(0);
    	String name=c.getName();
    		
    	Content content=BubbleDocs.parseName(name);
    	try{
    		content.importFromXML(c);
    	}catch(NullPointerException e){ System.out.println(String.format("Unknown content type %s", name));}
    	
    	this.setContent(content);
    
    }

    /**
     * @return
     */
    public Element exportToXML() {
    	Element element = new Element("cell");
    	element.setAttribute("id", this.getId());
    	element.setAttribute("line", this.getLine().toString());
    	element.setAttribute("column", this.getColumn().toString());
    	element.setAttribute("protect", this.getProtect().toString());
    	if(this.getContent()!=null)
    		element.addContent(this.getContent().exportToXML());
    	
	return element;
    }
}
