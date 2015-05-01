package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;


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
	 */
	public abstract Element exportToXML();
	

	/**
	 * @return
	 * @throws NullContentException
	 */
	public abstract int getValue() throws NullContentException;
	/**
	 * @param c
	 */
	public abstract void importFromXML(Element c);
	
	public abstract void accept(CalcSheetExporter exporter);

	public void delete() {
		if(this.getCell() !=null)
		this.setCell(null);
		this.deleteDomainObject();
		
	}
    
}
