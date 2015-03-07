package pt.tecnico.bubbledocs.content;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exceptions.NullContentException;

public class Avg extends RangeFunction {

	@Override
	public int getValue() throws NullContentException {
		int total = 0;
		int n = 0;
		
		for(Content c: this.getRangeValues()){
			total += c.getValue();
			n += 1;
		}
		
		return total / n;
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
