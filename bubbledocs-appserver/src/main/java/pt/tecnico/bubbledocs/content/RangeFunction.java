package pt.tecnico.bubbledocs.content;


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
	
	public Iterable<Content> getRangeValues(){
		return range.getIterable();
	}
}
