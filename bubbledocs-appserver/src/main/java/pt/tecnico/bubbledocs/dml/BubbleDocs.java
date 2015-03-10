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
	
    private BubbleDocs() {
    	FenixFramework.getDomainRoot().setBubbledocs(this);
    	this.setIdCounter(1);
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

    //author is the user that is doing the action.
	public void addReader(User author, String userName, CalcSheet calcSheet) {
		//TODO
    	//PRECOND: author owns or can write this file
    	//PRECOND: username is not already in this association
		
	}

	public void addWriter(User author, String username, CalcSheet calcSheet) {
		// TODO Auto-generated method stub
		//PRECOND: author owns or can write this file
    	//PRECOND: username MUST be able to read this file		
	}

	public void removeReader(User author, String username, CalcSheet calcSheet) {
		// TODO Auto-generated method stub
		//PRECOND: author owns or can write this file
    	//PRECOND: username can read this file and CANNOT write this file
		
	}

	public void removeWriter(User author, String username, CalcSheet calcSheet) {
		// TODO Auto-generated method stub
		//PRECOND: author owns or can write this file
    	//PRECOND: username can write this file.
	}
}
