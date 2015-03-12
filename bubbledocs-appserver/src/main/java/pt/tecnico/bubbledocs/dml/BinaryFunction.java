package pt.tecnico.bubbledocs.dml;

import java.util.List;

import org.jdom2.Element;

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
public class BinaryFunction extends BinaryFunction_Base {
    
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
    

	
	/* (non-Javadoc)
	 * @see pt.tecnico.bubbledocs.dml.Function#importFromXML(org.jdom2.Element)
	 */
	@Override
	public void importFromXML(Element element) {	
    	List<Element> addElement = element.getChildren();
    	Element c=addElement.get(0);
    	String name=c.getName();
    	Content content1=BubbleDocs.parseName(name);
    	try{
    		content1.importFromXML(c);
    		this.setArgument1((FunctionArgument)content1);
    	}catch(NullPointerException e){ System.out.println(String.format("Unknown content type %s", name));}
    	
    	c=addElement.get(1);
    	name=c.getName();
    	Content content2=BubbleDocs.parseName(name);
    	try{
    		content2.importFromXML(c);
    		this.setArgument2((FunctionArgument)content2);
    	}catch(NullPointerException e){ System.out.println(String.format("Unknown content type %s", name));}
    	
	}
}
