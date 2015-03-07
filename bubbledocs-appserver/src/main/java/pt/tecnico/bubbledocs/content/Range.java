package pt.tecnico.bubbledocs.content;

import java.util.ArrayList;

import pt.tecnico.bubbledocs.dml.CalcSheet;

public class Range {
	public Range(){
		
	}
	
	public Range (int firstLine, int firstColumn, int lastLine,
			int lastColumn, CalcSheet sheet){
		
		init(firstLine, firstColumn, lastLine, lastColumn, sheet);
	}
	
	public void init(int firstLine, int firstColumn, int lastLine,
			int lastColumn, CalcSheet sheet){
		
		//FIXME: need additional getters on CalcSheet
	}
	
	/*
	Duarte: if _all_ we want is to iterate, we can abstract from the
	specific implementation (array? arraylist? etc.)
	and return a generic iterable
	*/
	public Iterable<Content> getIterable(){
		
		// FIXME
		// complete stub method
		return new ArrayList<Content>();
	}
}
