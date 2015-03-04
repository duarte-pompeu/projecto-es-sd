package pt.tecnico.bubbledocs.dml;

import pt.tecnico.bubbledocs.content.Content;

public class Cell extends Cell_Base {
    private final int column, line;
   
    public Cell(int column, int line) {
    	super();
    	this.line = line;
        this.column = column;
    }
    
    public Content getContent(){
		//FIXME: implement method properly
		
		return null;
	}
    
    @Override
    public Integer getLine(){
    	return line;
    }
    
    @Override
    public Integer getColumn(){
    	return column;
    }
    
}
