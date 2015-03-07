package pt.tecnico.bubbledocs.content;

import java.util.Iterator;

public abstract class RangeFunction extends Function {
	private Range range;
	
	public RangeFunction(){
		
	}
	
	public RangeFunction(Range range){
		setRange(range);
	}
	
	public void setRange(Range range){
		this.range = range;
	}
	
	public Iterator<Content> getRangeValues(){
		return range.getIterator();
	}
}
