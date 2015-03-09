package pt.tecnico.bubbledocs.dml;

import java.util.List;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exceptions.NullContentException;

public class Reference extends Reference_Base {
    
    public Reference() {
        super();
    }
    
	
	public Reference(Cell cell){
		init(cell);
	}
	
	private void init(Cell cell){
		this.setCell(cell);
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
		referenceElement.addContent(this.getCell().exportToXML());
		return referenceElement;
	}
	public void importFromXML(Element element){
		
		List<Element> cellsElement = element.getChildren();
    	Element cellElement=cellsElement.get(0);
    	Cell cell;
    	int id= Integer.parseInt(cellElement.getAttributeValue("id"));
    	if((cell=BubbleDocs.currentSheet.getCell(id))!=null){
    		this.setCell(cell);
    	}
    	
    	else{
    		cell=new Cell();
    		cell.importFromXML(cellElement);
    		this.setCell(cell);
    	}
		
	}
}
    

