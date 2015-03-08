package pt.tecnico.bubbledocs.dml;


public class RangeFunction extends RangeFunction_Base {
    
    public RangeFunction() {
        super();
    }
    public RangeFunction(Range range){
		init(range);
	}
	
	private void init(Range range){
		this.setRange(range);
	}
	
	public Iterable<Cell> getRangeCells(){
		return this.getRange().getCellsSet();
	}
}
