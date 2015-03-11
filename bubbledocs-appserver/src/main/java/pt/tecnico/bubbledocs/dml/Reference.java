package pt.tecnico.bubbledocs.dml;

import java.util.List;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.NullContentException;

public class Reference extends Reference_Base {
    
    public Reference() {
        super();
    }
    
	public Reference(Cell cell){
		super();
		init(cell);
	}
	
	private void init(Cell cell){
		this.setPointedCell(cell);
	}
	
	public int getValue() throws NullContentException{
		Content content = this.getPointedCell().getContent();
		
		if(content == null){
			int line = this.getPointedCell().getLine();
			int column = this.getPointedCell().getColumn();
			throw new NullContentException(line,column);
		}
		else{
			return content.getValue();
		}
	}
	
	@Override
	public String toString(){
		return "=" + this.getPointedCell().getColumn()
				+ ";" + this.getPointedCell().getLine();
	}
	
	public Element exportToXML(){
		Element referenceElement=new Element("reference");
		referenceElement.addContent(this.getPointedCell().exportToXML());
		return referenceElement;
	}
	
	public void importFromXML(Element element){
		
		List<Element> cellsElement = element.getChildren();
    	Element cellElement=cellsElement.get(0);
    	Cell cell;
    	
    	if(cellElement==null)
    		return;
    	
    	int line= Integer.parseInt(cellElement.getAttributeValue("line"));
    	int column= Integer.parseInt(cellElement.getAttributeValue("column"));
    	
    	try {
    		cell=BubbleDocs.currentSheet.getCell(line, column);
    		this.setPointedCell(cell);
    	}
    	catch(NotFoundException e){
    	
    		cell=new Cell();
    		cell.importFromXML(cellElement);
    		this.setPointedCell(cell);
    	}
	}
}
    

