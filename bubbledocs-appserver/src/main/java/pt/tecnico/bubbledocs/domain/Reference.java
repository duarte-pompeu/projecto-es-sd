package pt.tecnico.bubbledocs.domain;

import java.util.List;

import org.jdom2.Element;


import pt.tecnico.bubbledocs.exceptions.*;

/**
 * @author pc-w
 *
 */
public class Reference extends Reference_Base {
    
    /**
     * 
     */
    public Reference() {
        super();
    }
    
	/**
	 * @param cell
	 */
	public Reference(Cell cell){
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
		
		setPointedCell(null);
		if(this.getCell() !=null)
    	setCell(null);
		
    	deleteDomainObject();
        }
	
	/* (non-Javadoc)
	 * @see pt.tecnico.bubbledocs.dml.FunctionArgument#getValue()
	 */
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
	
	/* (non-Javadoc)
	 * @see pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject#toString()
	 */
	@Override
	public String toString(){
		return "=" + this.getPointedCell().getColumn()
				+ ";" + this.getPointedCell().getLine();
	}
	
	public void accept(CalcSheetExporter exporter) {
		exporter.exportReference(this);
	}
}
    

