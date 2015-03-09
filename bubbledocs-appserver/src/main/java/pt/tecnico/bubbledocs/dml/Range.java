package pt.tecnico.bubbledocs.dml;

import java.util.List;

import org.jdom2.Element;

public class Range extends Range_Base {
    
    public Range() {
        super();
    }
    

	public Range (int firstLine, int firstColumn, int lastLine,
			int lastColumn, CalcSheet sheet){
		super();
		init(firstLine, firstColumn, lastLine, lastColumn, sheet);
	}
	
	public void init(int firstLine, int firstColumn, int lastLine,
			int lastColumn, CalcSheet sheet){
		
		int column;
		int line;
		
		
		//TODO: make sure logic is right
		for(line = firstLine; line <= lastLine; line++){
			for(column = firstColumn; column <= lastColumn; column++){
				this.getCellsSet().add(sheet.getCell(line, column));
			}
		}
	}
	
	/*
	Duarte: if all we want is to iterate, we can abstract from the
	specific implementation (array? arraylist? etc.)
	and return a generic iterable
	*/
	public Iterable<Cell> getIterable(){
		return this.getCellsSet();
	}


	public Element exportToXML() {
		Element element = new Element("range");
    
    	for(Cell c: this.getCellsSet())
    		element.addContent(c.exportToXML());
    	
    	return element;
	}
	
	public void importFromXML(Element element){
		
		
		List<Element> cells = element.getChildren();
    	
    	for (Element cell : cells) {
    	    Cell c = new Cell();
    	    c.importFromXML(cell);
    	    this.addCells(c);
    	}
	}
	
}
