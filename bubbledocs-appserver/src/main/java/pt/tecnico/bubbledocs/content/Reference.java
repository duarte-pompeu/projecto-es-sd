package pt.tecnico.bubbledocs.content;

import pt.tecnico.bubbledocs.dml.Cell;
import pt.tecnico.bubbledocs.exceptions.NullContentException;

public class Reference extends SimpleContent {
	private Cell cell;
	
	public Reference(Cell cell){
		this.cell = cell;
	}
	
	public int getValue() throws NullContentException{
		Content content = cell.getContent();
		
		if(content == null){
			int x = cell.getColumn();
			int y = cell.getLine();
			throw new NullContentException(x,y);
		}
		else{
			return content.getValue();
		}
	}
}
