package pt.tecnico.bubbledocs.domain;

import pt.tecnico.bubbledocs.exceptions.NullContentException;

public class ReferenceArgument extends ReferenceArgument_Base {
    
    public ReferenceArgument() {
        super();
    }

	public ReferenceArgument(Cell cell){
		super();
		init(cell);
	}
	
	/**
	 * @param cell
	 */
	private void init(Cell cell){
		this.setPointedCell(cell);
	}
	
    
    public void delete() {
		
    	this.setBinaryFunction_(null);
    	this.setBinaryFunction(null);
    	
		setPointedCell(null);
	
		
    	deleteDomainObject();
        }

    
    public int getValue() throws NullContentException{
		Content content = this.getPointedCell().getContent();
		
		if(content == null){
			int line = this.getPointedCell().getLine();
			int column = this.getPointedCell().getColumn();
			throw new NullContentException(line,column);
		}
		else{
			return content.getValue();
		}
	}
    
    @Override
	public String toString(){
		return "=" + this.getPointedCell().getColumn()
				+ ";" + this.getPointedCell().getLine();
	}
	
	@Override
	public void accept(CalcSheetExporter exporter) {
		exporter.exportReferenceArgument(this);
	}
}
