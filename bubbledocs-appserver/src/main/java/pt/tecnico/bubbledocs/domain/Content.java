package pt.tecnico.bubbledocs.domain;


import pt.tecnico.bubbledocs.exceptions.NullContentException;

/**
 * @author pc-w
 *
 */
public abstract class Content extends Content_Base {
    
    /**
     * 
     */
    public Content() {
        super();
    }
	

	/**
	 * @return
	 * @throws NullContentException
	 */
	public abstract int getValue() throws NullContentException;
	
	public abstract void accept(CalcSheetExporter exporter);

	public void delete() {
		if(this.getCell() !=null)
		this.setCell(null);
		this.deleteDomainObject();
		
	}
    
}
