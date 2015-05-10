package pt.tecnico.bubbledocs.domain;


/**
 * @author pc-w
 *
 */
/**
 * @author pc-w
 *
 */
/**
 * @author pc-w
 *
 */
public abstract class BinaryFunction extends BinaryFunction_Base {
    
    /**
     * 
     */
    public BinaryFunction() {
        super();
    }
    
    /**
     * @param arg1
     * @param arg2
     */
    public BinaryFunction(FunctionArgument arg1, FunctionArgument arg2){
		super();
    	init(arg1, arg2);
	}
	
	/**
	 * @param arg1
	 * @param arg2
	 */
	protected void init(FunctionArgument arg1, FunctionArgument arg2){
		this.setArgument1(arg1);
		this.setArgument2(arg2);
	}
	
	/**
	 * @return
	 */
	public FunctionArgument getArg1(){	return this.getArgument1(); }
	/**
	 * @return
	 */
	public FunctionArgument getArg2(){	return this.getArgument2(); }
    
	public void delete() {
		
		if(this.getArgument1() !=null)
		this.setArgument1(null);
		if(this.getArgument2() !=null)
		this.setArgument2(null);
		if(this.getCell() !=null)
		this.setCell(null);
		this.deleteDomainObject();
		
	}
	
	public void accept(CalcSheetExporter exporter) {
		exporter.exportBinaryFunction(this);
	}
}
