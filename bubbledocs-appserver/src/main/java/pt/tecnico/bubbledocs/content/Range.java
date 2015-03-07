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
		
		int i;
		int j;
		
		cells = new ArrayList<Cell>();
		
		for(i = firstColumn; i < lastColumn; i++){
			for(j = firstLine; j < lastLine; j++){
				
			}
		}
		
		//FIXME: need additional getters on CalcSheet
	}
	
	/*
	Duarte: if _all_ we want is to iterate, we can abstract from the
	specific implementation (array? arraylist? etc.)
	and return a generic iterable
	*/
	public Iterable<Cell> getIterable(){
		
		// FIXME
		// complete stub method
		return cells;
	}
}
