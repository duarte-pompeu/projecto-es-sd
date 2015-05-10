package pt.tecnico.bubbledocs.domain;

import pt.tecnico.bubbledocs.exceptions.NullContentException;

/**
 * @author pc-w
 *
 */
public class Avg extends Avg_Base {
    
    /**
     * 
     */
    public Avg() {
        super();
    }
    
    /**
     * @param range
     */
    public Avg(Range range){
		super();
    	super.init(range);
	}
	
	/* (non-Javadoc)
	 * @see pt.tecnico.bubbledocs.dml.Function#getValue()
	 */
	@Override
	public int getValue() throws NullContentException {
		int total = 0;
		int n = 0;
		
		for(Cell cell: this.getRangeCells()){
			total += cell.getContent().getValue();
			n += 1;
		}
		
		return total / n;
	}
	
	@Override
	public String getName() {
		return "avg";
	}

	
}
