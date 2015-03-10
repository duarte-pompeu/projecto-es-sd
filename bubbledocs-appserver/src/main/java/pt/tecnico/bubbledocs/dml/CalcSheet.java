package pt.tecnico.bubbledocs.dml;

import org.jdom2.Element;
import org.joda.time.LocalDate;
import pt.tecnico.bubbledocs.exceptions.*;
import java.util.*;



public class CalcSheet extends CalcSheet_Base {
		
	//Again, do you need an empty constructor in the Framework?
	public CalcSheet() {
		super();
	}
	
    public CalcSheet(boolean protection, String name, int lines, int columns, LocalDate date) {
        //TODO
    	super();
        this.setProtection(protection);
    	//get unique id
    	//this.setId(something that generates a unique id)
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
    
    public Cell getCell(int line, int column) {
    	if (outsideBounds(line, column)) {
    		throw new IllegalArgumentException("Out of bounds");
    	}
    	
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
   
    public Content getContent(int line, int column) {
    	//TODO
    	if (outsideBounds(line, column)) {
    		throw new IllegalArgumentException("Out of bounds");
    	}
    	
    	return this.getCellByIndex(line, column).getContent();
    }
    
    //this may be changed to receive a string, and using a Content factory, create the Content
    // *whip* *whip* get that Parser working Tiago :P
    public void setContent(int line, int column, Content content) {
    	if (outsideBounds(line, column)) {
    		throw new IllegalArgumentException("Out of bounds");
    	}
    	
    	this.getCellByIndex(line, column).setContent(content);
    }
    
    //NOTE: should these 3 methods be moved to BubbleDocs?
    /*
     * This adds another user to the list of users that can use this file.
     * The file permission can be read-only or read-write
     */
    public void addReader(User username) {
    	//PRECOND: author owns or can write this file
    	//PRECOND: username is not already in this list
    	//TODO
    }
    
    public void addWriter(User username) {
    	//PRECOND: author owns or can write this file
    	//PRECOND: username must be able to read this file
    }
    
    /*
     * This removes a user that is in the list of users that can use 
     * a calcsheet.
     */
    public void removeReader(String username) {
    	//PRECOND: username can use this file
    	//PRECOND: this user owns id or can write id
    	//TODO
    }
    
    public void removeWriter(String username) {
    	//TODO
    }
    
    private boolean outsideBounds(int line, int column) {
    	return line < 1 || column < 1 || line > this.getLines() || column > this.getColumns();
    }
    
    private Cell getCellByIndex(int line, int column) {
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
