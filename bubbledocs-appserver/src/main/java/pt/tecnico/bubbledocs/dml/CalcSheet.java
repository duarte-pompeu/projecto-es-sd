package pt.tecnico.bubbledocs.dml;

import pt.tecnico.bubbledocs.content.Content;
import pt.tecnico.bubbledocs.exceptions.PermissionException;
import java.util.*;

public class CalcSheet extends CalcSheet_Base {
	
	//When a user requests a file by calling user.getCalcSheet(id) and only has readonly access	
	private transient boolean readonly = false;
	private transient User actualUser;
	
	private List<Cell> cells;
	
	//Again, do you need an empty constructor in the Framework?
	public CalcSheet() {
		super();
		//put the existing cells in the list
	}
	
    public CalcSheet(String name, int lines, int columns) {
        //TODO
    	super();
        //get unique id
    	//this.setId(something that generates a unique id)
    	this.setName(name);
    	this.setLines(lines);
    	this.setColumns(columns);
    
    }
    
    protected void setReadonly() {
    	this.readonly = true;
    }
    
    protected void setReadonly(boolean readonly) {
    	this.readonly = readonly;
    }
    
    protected boolean getReadonly() {
    	return readonly;
    }
    
    protected void setActualUser(User user) {
    	this.actualUser = user;
    }
    
    protected User getActualUser(User user) {
    	return this.actualUser;
    }
 
    public Cell getCell(int line, int column) {
      //TODO
      return null;
    }
   
    public Content getContent(int line, int column) {
    	//TODO
    	if (outsideBounds(line, column)) {
    		throw new IllegalArgumentException("Invalid coordinate");
    	}
    	//return cells.get((line-1)*columns+(column-1)).getContent();
    	return null;
    }
    
    //this may be changed to receive a string, and using a Content factory, create the Content
    // *whip* *whip* get that Parser working Tiago :P
    public void setContent(int line, int column, Content content) {
    	//TODO
    	if (outsideBounds(line, column)) {
    		throw new IllegalArgumentException("Invalid coordinate");
    	}
    	if (readonly) {
    		throw new PermissionException();
    	}
    	//something something
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
    	//return line < 1 || column < 1 || line > lines || column > columns;
    	return true;
    }    
    
}
