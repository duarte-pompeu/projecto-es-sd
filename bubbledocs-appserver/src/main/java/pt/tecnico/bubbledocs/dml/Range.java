package pt.tecnico.bubbledocs.dml;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.jdom2.Element;

/**
 * @author pc-w
 *
 */
public class Range extends Range_Base implements Iterable<Cell>{
    
    /**
     * 
     */
    public Range() {
        super();
    }
    

	/**
	 * @param firstLine
	 * @param firstColumn
	 * @param lastLine
	 * @param lastColumn
	 * @param sheet
	 */
	public Range (int firstLine, int firstColumn, int lastLine,
			int lastColumn, CalcSheet sheet){
		super();
		init(firstLine, firstColumn, lastLine, lastColumn, sheet);
	}
	
	/**
	 * @param firstLine
	 * @param firstColumn
	 * @param lastLine
	 * @param lastColumn
	 * @param sheet
	 */
	public void init(int firstLine, int firstColumn, int lastLine,
			int lastColumn, CalcSheet sheet){
		
		int column;
		int line;
		
		
		//TODO: THIS NEXT CODE WAS BEFORE THE RECENT CHANGE. CHECK THE DML FILE
	/*	for(line = firstLine; line <= lastLine; line++){
			for(column = firstColumn; column <= lastColumn; column++){
				this.getCellsSet().add(sheet.getCell(line, column));
			}
		}*/ 
	}
	
	/*
	Duarte: if all we want is to iterate, we can abstract from the
	specific implementation (array? arraylist? etc.)
	and return a generic iterable
	*/
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Cell> iterator(){
		int upperLine, leftmostColumn, downLine, rightmostColumn;
		ArrayList<Cell> a=new ArrayList<Cell>();
		
		upperLine=this.getLeftUpCell().getLine();
		leftmostColumn=this.getLeftUpCell().getColumn();
		downLine=this.getRightDownCell().getLine();
		rightmostColumn=this.getRightDownCell().getColumn();
		
		for(int i=1; i<=downLine; i++)
			for(int j=1; j<=rightmostColumn; j++)
				a.add(this.getCalcSheet().getCell(i, j));
		
		return a.iterator();
	}


	/**
	 * @return
	 */
	public Element exportToXML() {
		Element element = new Element("range");
    
    	Cell c= this.getLeftUpCell();
    	element.addContent(c.exportToXML());
    	c=this.getRightDownCell();
    	element.addContent(c.exportToXML());
    	
    	return element;
	}
	
	/**
	 * @param element
	 */
	public void importFromXML(Element element){
		
		
		List<Element> cells = element.getChildren();
    	
    	Element cell1=cells.get(0);
    	Cell cell;
    	int id= Integer.parseInt(cell1.getAttributeValue("id"));
    	if((cell=BubbleDocs.currentSheet.getCell(id))!=null){
    		this.setLeftUpCell(cell);
    	}
    	
    	else{
    		cell=new Cell();
    		cell.importFromXML(cell1);
    		this.setLeftUpCell(cell);
    	}
    	

    	Element cell2=cells.get(1);
    	
    	id= Integer.parseInt(cell2.getAttributeValue("id"));
    	if((cell=BubbleDocs.currentSheet.getCell(id))!=null){
    		this.setRightDownCell(cell);
    	}
    	
    	else{
    		cell=new Cell();
    		cell.importFromXML(cell2);
    		this.setRightDownCell(cell);
    	}
    	
	}
	
}
