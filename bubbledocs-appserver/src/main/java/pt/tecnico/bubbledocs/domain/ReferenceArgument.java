package pt.tecnico.bubbledocs.domain;

import java.util.List;

import org.jdom2.Element;


import pt.tecnico.bubbledocs.exceptions.NullContentException;

public class ReferenceArgument extends ReferenceArgument_Base {
    
    public ReferenceArgument() {
        super();
    }

	public ReferenceArgument(Cell cell){
		super();
		init(cell);
	}
	
	/**
	 * @param cell
	 */
	private void init(Cell cell){
		this.setPointedCell(cell);
	}
	
    
    public void delete() {
		
    	this.setBinaryFunction_(null);
    	this.setBinaryFunction(null);
    	
		setPointedCell(null);
	
		
    	deleteDomainObject();
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
	
	/* (non-Javadoc)
	 * @see pt.tecnico.bubbledocs.dml.FunctionArgument#exportToXML()
	 */
	public Element exportToXML(){
		Element referenceElement=new Element("referenceArgument");
		referenceElement.addContent(this.getPointedCell().exportToXML());
		return referenceElement;
	}
	
	/* (non-Javadoc)
	 * @see pt.tecnico.bubbledocs.dml.FunctionArgument#importFromXML(org.jdom2.Element)
	 */
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
    	catch(RuntimeException e){
    	
    		cell=new Cell();
    		cell.importFromXML(cellElement);
    		this.setPointedCell(cell);
    	}
	}
}
