package pt.tecnico.bubbledocs.content;

import pt.tecnico.bubbledocs.dml.Cell;


public abstract class RangeFunction extends Function {
	private Range range;
	
	public RangeFunction(){
		
	}
	
	public RangeFunction(Range range){
		init(range);
	}
	
	private void init(Range range){
		this.range = range;
	}
	
	public Iterable<Cell> getRangeCells(){
		return range.getIterable();
	}
}
