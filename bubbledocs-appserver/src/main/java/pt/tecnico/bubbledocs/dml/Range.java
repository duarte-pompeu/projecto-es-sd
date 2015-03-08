package pt.tecnico.bubbledocs.dml;

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
}
