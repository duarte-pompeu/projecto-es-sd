package pt.tecnico.bubbledocs.content;

import org.jdom2.Element;

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
	
	@Override
	public String toString(){
		return "=" + cell.getColumn()
				+ ";" + cell.getLine();
	}
	
	public Element exportToXML(){return null;}
	public void importFromXML(Element element){};
}
