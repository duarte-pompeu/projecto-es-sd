package pt.tecnico.bubbledocs.content;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.dml.Cell;
import pt.tecnico.bubbledocs.exceptions.NullContentException;

public class Reference extends SimpleContent {
	private Cell cell;
	
	public Reference(){
		
	}
	
	public Reference(Cell cell){
		init(cell);
	}
	
	private void init(Cell cell){
		this.cell = cell;
	}
	
	public int getValue() throws NullContentException{
		Content content = cell.getContent();
		
		if(content == null){
			int line = cell.getLine();
			int column = cell.getColumn();
			throw new NullContentException(line,column);
		}
		else{
			return content.getValue();
		}
	}
	
	@Override
	public String toString(){
		return "=" + cell.getColumn()
				+ ";" + cell.getLine();
	}
	
	public Element exportToXML(){return null;}
	public void importFromXML(Element element){};
}
