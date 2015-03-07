package pt.tecnico.bubbledocs.content;

import java.util.ArrayList;

import pt.tecnico.bubbledocs.dml.CalcSheet;
import pt.tecnico.bubbledocs.dml.Cell;

public class Range {
	private ArrayList<Cell> cells;
	public Range(){
		
	}
	
	public Range (int firstLine, int firstColumn, int lastLine,
			int lastColumn, CalcSheet sheet){
		
		init(firstLine, firstColumn, lastLine, lastColumn, sheet);
	}
	
	public void init(int firstLine, int firstColumn, int lastLine,
			int lastColumn, CalcSheet sheet){
		
		int column;
		int line;
		
		cells = new ArrayList<Cell>();
		
		//TODO: make sure logic is right
		for(line = firstLine; line <= lastLine; line++){
			for(column = firstColumn; column <= lastColumn; column++){
				cells.add(sheet.getCell(line, column));
			}
		}
	}
	
	/*
	Duarte: if all we want is to iterate, we can abstract from the
	specific implementation (array? arraylist? etc.)
	and return a generic iterable
	*/
	public Iterable<Cell> getIterable(){
		return cells;
	}
}
