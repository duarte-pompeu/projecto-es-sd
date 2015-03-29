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
	public void importFromXML(Element literalElement) {
		int value = Integer.parseInt(literalElement.getAttribute("val").getValue());
    	init(value);
	}
	
	@Override
	public Element exportToXML() {
		Element element = new Element("literalArgument");
    	element.setAttribute("val", this.getVal().toString());
	return element;
	}
    
}
