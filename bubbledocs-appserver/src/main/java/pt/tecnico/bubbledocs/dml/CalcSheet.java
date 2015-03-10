package pt.tecnico.bubbledocs.dml;

import org.jdom2.Element;
import org.joda.time.LocalDate;
import pt.tecnico.bubbledocs.exceptions.*;
import java.util.*;



public class CalcSheet extends CalcSheet_Base {
		
	public CalcSheet() {
		super();
	}
	
    public CalcSheet(boolean protection, String name, int lines, int columns, LocalDate date) {
        //TODO
    	super();
        this.setProtection(protection);
    	//get unique id
    	//BubbleDocs.getInstance().getUniqueId();
    	this.setName(name);
    	this.setLines(lines);
    	this.setColumns(columns);
    	this.setDate(date);
    	for (int i=0; i<lines; ++i) {
    		for (int j=0; j<columns; ++j) {
    			this.addCell(new Cell(i, j));
    		}
    	}    
    }
    
    //This method shouldn't be used by a user.
    public Cell getCell(int line, int column) {    	
    	return this.getCellByIndex(line, column); 
    }

    public Cell getCell(int id) {

    	for(Cell c : this.getCellSet()) {
    		if(c.getId().intValue()==id)
    			return c;
    	}
    	return null;

    }

    public boolean hasCell(int id) {

    	for(Cell c : this.getCellSet()) {
    		if(c.getId().intValue()==id)
    			return true;
    	}
    	return false;
    }

    public Content getContent(User reader, int line, int column) {
    	if (!(reader.getReadableCalcSheetSet().contains(this))) throw new PermissionException();
    	
    	return this.getCellByIndex(line, column).getContent();
    }
    
    //this may be changed to receive a string, and using a Content factory, create the Content
    // *whip* *whip* get that Parser working Tiago :P
    public void setContent(User writer, Content content, int line, int column) {    	
    	if (!(writer.getWriteableCalcSheetSet().contains(writer))) throw new PermissionException();
    	
    	this.getCellByIndex(line, column).setContent(content);
    }
    
    
    //These methods are implemented in BubbleDocs because users are only supposed
    //to get their own user.
    
    /*
     * This adds another user to the list of users that can use this file.
     * The file permission can be read-only or read-write
     */
    public void addReader(User author, String username) {
    	//PRECOND: author owns or can write this file
    	//PRECOND: username is not already in this list
    	BubbleDocs.getInstance().addReader(author, username, this);    	
    }
    
    

	public void addWriter(User author, String username) {
    	//PRECOND: author owns or can write this file
    	//PRECOND: username MUST be able to read this file
		BubbleDocs.getInstance().addWriter(author, username, this);
    }
    
    /*
     * This removes a user that is in the list of users that can use 
     * a calcsheet.
     */
    public void removeReader(User author, String username) {
    	//PRECOND: author owns or can write this file
    	//PRECOND: username can read this file and CANNOT write this file
    	BubbleDocs.getInstance().removeReader(author, username, this);
    }
    
    public void removeWriter(User author, String username) {
    	//PRECOND: author owns or can write this file
    	//PRECOND: username can read this file and CANNOT write this file
    	BubbleDocs.getInstance().removeWriter(author, username, this);
    }
    
    private boolean outsideBounds(int line, int column) {
    	return line < 1 || column < 1 || line > this.getLines() || column > this.getColumns();
    }
    
    private Cell getCellByIndex(int line, int column) {
    	if (outsideBounds(line, column)) {
    		throw new IllegalArgumentException("Out of bounds");
    	}
    	
    	for (Cell cell : this.getCellSet()) {
    		if (cell.getLine() == line && cell.getColumn() == column) {
    			return cell;
    		}
    	}
    	throw new NotFoundException();
    }
    
    public void importFromXML(Element calcSheetElement) {
    	this.setDate(new LocalDate(calcSheetElement.getAttribute("date").getValue()));
    	this.setId(new Integer(calcSheetElement.getAttribute("id").getValue()));
    	this.setName(new String(calcSheetElement.getAttribute("name").getValue()));
    	this.setLines(new Integer(calcSheetElement.getAttribute("lines").getValue()));
    	this.setColumns(new Integer(calcSheetElement.getAttribute("columns").getValue()));
    	this.setProtection(new Boolean(calcSheetElement.getAttribute("protection").getValue()));
    	
    	//to aid in the importation of references and ranges
    	BubbleDocs.currentSheet=this;
    	
    	List<Element> cells = calcSheetElement.getChildren();
    	
    	for (Element cell : cells) {
    	    Cell c = new Cell();
    	    c.importFromXML(cell);
    	    addCell(c);
    	}
    	
    }

    public Element exportToXML() {
    	Element element = new Element("calcSheet");
    	try{
    	
    	//element.setAttribute("date", this.getDate().toString());
    	//element.setAttribute("id", this.getId().toString());
    	element.setAttribute("name", this.getName().toString());
    	element.setAttribute("lines", this.getLines().toString());
    	element.setAttribute("columns", this.getColumns().toString());
    	//element.setAttribute("protection", this.getProtection().toString());
    	}catch(Exception e){System.out.println(e.toString());}
    	
    	for(Cell c: this.getCellSet())
    		element.addContent(c.exportToXML());
	
    	return element;
    	}
    
}
