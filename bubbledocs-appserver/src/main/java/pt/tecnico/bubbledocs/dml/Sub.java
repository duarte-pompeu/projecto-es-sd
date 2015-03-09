package pt.tecnico.bubbledocs.dml;

import java.util.List;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exceptions.NullContentException;

public class Sub extends Sub_Base {
    
    public Sub() {
        super();
    }
    
    
    public Sub(SimpleContent arg1, SimpleContent arg2){
		super.init(arg1, arg2);
	}
	
	@Override
	public int getValue() throws NullContentException {
		int val1 = getArg1().getValue();
		int val2 = getArg2().getValue();
		
		return val1 - val2;
	}

	@Override
	public Element exportToXML() {
		Element element = new Element("sub");
    	element.addContent(this.getArg1().exportToXML());
    	element.addContent(this.getArg2().exportToXML());
	return element;
	}

	@Override
	public void importFromXML(Element element) {
		List<Element> addElement = element.getChildren();
    	Element c=addElement.get(0);
    	String name=c.getName();
    	Content content1=BubbleDocs.parseName(name);
    	try{
    		content1.importFromXML(c);
    		this.addArguments((SimpleContent)content1);
    	}catch(NullPointerException e){ System.out.println(String.format("Unknown content type %s", name));}
    	
    	c=addElement.get(1);
    	name=c.getName();
    	Content content2=BubbleDocs.parseName(name);
    	try{
    		content2.importFromXML(c);
    		this.addArguments((SimpleContent)content2);
    	}catch(NullPointerException e){ System.out.println(String.format("Unknown content type %s", name));}
	}
}
