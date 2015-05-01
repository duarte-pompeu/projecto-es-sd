package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;



public class LiteralArgument extends LiteralArgument_Base {
    
    public LiteralArgument() {
        super();
    }

    public LiteralArgument(int value){
  		super();
      	init(value);
  	}
  	
  	/**
  	 * @param value
  	 */
  	private void init(int value){
  		this.setVal(value);
  	}
  	
  	/* (non-Javadoc)
  	 * @see pt.tecnico.bubbledocs.dml.FunctionArgument#getValue()
  	 */
  	public int getValue(){
  		return this.getVal();
  	}
  	
  	@Override
  	public void accept(CalcSheetExporter exporter) {
  		exporter.exportLiteralArgument(this);
  	}

	@Override
	public void importFromXML(Element literalElement) {
		int value = Integer.parseInt(literalElement.getAttribute("val").getValue());
    	init(value);
	}
    
}
