package pt.tecnico.bubbledocs.dml;

import pt.tecnico.bubbledocs.content.Content;

import java.util.*;

public class CalcSheet extends CalcSheet_Base {
    //this should be in .dml
	//int id
	//String name
	//uncommented to remove errors
	int lines;
	int columns;
	//
	
	//When a user requests a file by calling user.getCalcSheet(id) and only has readonly access	
	private boolean readonly = false;
	
	private List<Cell> cells;
	
	//Again, do you need an empty constructor in the Framework?
	public CalcSheet() {
		super();
		//put the existing cells in the list
	}
	
    public CalcSheet(int lines, int columns) {
        super();
        //get unique id
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
    		throw new /*Permission*/RuntimeException();
    	}
    	//something something
    }
    
    /*
     * This adds another user to the list of users that can use this file.
     * The file permission can be read-only or read-write
     */
    public void addUserToReadCalcSheet(User author, String username) {
    	//PRECOND: author owns or can write this file
    	//PRECOND: username is not already in this list
    	//TODO
    }
    
    public void addUserToReadWriteCalcSheet(User author, String username) {
    	//PRECOND: author owns or can write this file
    	//PRECOND: username must be able to read this file
    }
    
    /*
     * This removes a user that is in the list of users that can use 
     * a calcsheet.
     */
    public void removeUserToCalcSheet(User author, String username) {
    	//PRECOND: username can use this file
    	//PRECOND: this user owns id or can write id
    	//TODO
    }
    
    private boolean outsideBounds(int line, int column) {
    	return line < 1 || column < 1 || line > lines || column > columns;
    }
    
    
}
