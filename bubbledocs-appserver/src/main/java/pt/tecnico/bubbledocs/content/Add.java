package pt.tecnico.bubbledocs.content;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exceptions.NullContentException;

public class Add extends BinaryFunction {
	
	public Add(){
		super();
	}
	
	public Add(Content arg1, Content arg2){
		super(arg1, arg2);
	}
	
	@Override
	public int getValue() throws NullContentException {
		int val1 = this.getArg1().getValue();
		int val2 = this.getArg2().getValue();
		
		return val1 + val2;
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
