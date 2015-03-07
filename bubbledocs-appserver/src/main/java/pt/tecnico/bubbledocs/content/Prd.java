package pt.tecnico.bubbledocs.content;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.dml.Cell;
import pt.tecnico.bubbledocs.exceptions.NullContentException;

public class Prd extends RangeFunction {
	
	public Prd(){
		super();
	}
	
	public Prd(Range range){
		super(range);
	}
	
	@Override
	public int getValue() throws NullContentException {
		// initialize with 1 since we're multiplying
		int total = 1;
		
		for(Cell cell: this.getRangeCells()){
			total *= cell.getContent().getValue();
		}
		
		return total;
		
	}

	@Override
	public Element exportToXML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void importFromXML(Element element) {
		// TODO Auto-generated method stub

	}

}
