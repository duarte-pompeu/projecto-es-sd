package pt.tecnico.bubbledocs.dml;

import pt.ist.fenixframework.FenixFramework;

public class BubbleDocs extends BubbleDocs_Base {
	
	public static BubbleDocs getInstance() {
		BubbleDocs pb=null;
		
		//pb = FenixFramework.getDomainRoot().getBubbledocs();
		if(pb==null)
			pb = new BubbleDocs(); 
		
		return pb;
	    }
	
    public BubbleDocs() {
    	FenixFramework.getDomainRoot().setBubbledocs(this);
    }
    
    //for now only used for the xml import
    public static CalcSheet currentSheet;
    
    
    public static Content parseName(String name){
    	switch(name){
    	case "literal": return new Literal();
    	case "reference": return new Reference();
    	case "add": return new Add();
    	case "sub": return new Sub();
    	case "mul": return new Mul();
    	case "div": return new Div();
    	case "avg": return new Avg();
    	case "prd": return new Prd();
    	default: return null;
    	}	
    }
    
    public User login(String username, String password) {
    	//TODO
    	return null;
    }
    
    public User addUser(String userName, String name, String password) {
    	//TODO
    	//throw RepeatedIdentificationException case userName already exists
    	return null;
    }
    
    public void deleteUser(String userName) {
    	//TODO
    	//throw NotFoundException case userName doesn't exist
    }
}
