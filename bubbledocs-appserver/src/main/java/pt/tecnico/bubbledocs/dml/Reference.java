package pt.tecnico.bubbledocs.dml;

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
		this.getCell().exportToXML();
		return referenceElement;
	}
	public void importFromXML(Element element){
		//tricky. check if the cell has already been imported. 
		//if not, import it first. Some mechanism will be needed, probably id checking, 
		//in order not to double import a referenced cell.
	}
}
    

