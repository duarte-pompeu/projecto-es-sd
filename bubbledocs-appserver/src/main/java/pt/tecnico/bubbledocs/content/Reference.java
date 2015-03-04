package pt.tecnico.bubbledocs.content;

import pt.tecnico.bubbledocs.dml.Cell;

public class Reference extends SimpleContent {
	private Cell cell;
	
	public Reference(Cell cell){
		this.cell = cell;
	}
	
	public int getValue(){
		return cell.getContent().getValue();
	}
}
