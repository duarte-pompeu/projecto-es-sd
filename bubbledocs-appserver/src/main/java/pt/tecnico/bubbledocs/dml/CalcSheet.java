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
	//Dúvida, quando eu vou buscar um CalcSheet (por exemplo user.getCalcSheet(id)) é criado um objeto java novo?
	private boolean readonly = false;
	
	private List<Cell> cells;
		
    public CalcSheet(int lines, int columns) {
        super();
        //something should go here, not really sure. Fénix Framework is boggling my mind.

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
    	//return cells.get((line-1)*columns+(column-1));
    	return null;
    }
    
    private boolean outsideBounds(int line, int column) {
    	return line < 1 || column < 1 || line > lines || column > columns;
    }
    
    
}
