package pt.tecnico.bubbledocs.dml;

import pt.ist.fenixframework.Atomic;

import javax.transaction.*;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;

/**
 * @author pc-w
 *
 */
public class BubbleDocs extends BubbleDocs_Base {
	
	/**
	 * @return
	 */
	
	public static BubbleDocs getInstance() {
		
		
			
    		BubbleDocs pb;
		
    		pb = FenixFramework.getDomainRoot().getBubbleDocs();
    		if(pb==null)
    			pb = new BubbleDocs(); 
		
    		return pb;
		
    
	    	}
    	
    /**
     * 
     */
    private BubbleDocs() {
    	FenixFramework.getDomainRoot().setBubbleDocs(this);
    	this.setIdCounter(1);
    }
    
    //for now only used for the xml import
    /**
     * 
     */
    public static CalcSheet currentSheet;
    
    
    
    public CalcSheet getCalcSheetByName(String name) {
		for(CalcSheet c: this.getCalcSheetSet()){
			if(c.getName().equals(name))
				return c;
		}
		return null;
	}
    
    public void RemoveCalcSheet(String name) {
    	CalcSheet toRemove = getCalcSheetByName(name);
    	toRemove.getCellSet().clear();
    	this.removeCalcSheet(toRemove);
    }
    
    
    /**
     * @return
     */
    
    public synchronized int getUniqueId() {
    	int id = this.getIdCounter();
    	this.setIdCounter(id + 1);
    	return id;
    }
    
    /**
     * @param name
     * @return
     */
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
    
    /**
     * @param username
     * @param password
     * @return
     */
    public User login(String username, String password) {
    	//TODO
    	return null;
    }
    
    /**
     * @param userName
     * @param name
     * @param password
     * @return
     */
    public User addUser(String userName, String name, String password) {
    	//TODO
    	//throw RepeatedIdentificationException case userName already exists
    	return null;
    }
    
    /**
     * @param userName
     */
    public void deleteUser(String userName) {
    	//TODO
    	//throw NotFoundException case userName doesn't exist
    }

    //author is the user that is doing the action.
	/**
	 * @param author
	 * @param userName
	 * @param calcSheet
	 */
	public void addReader(User author, String userName, CalcSheet calcSheet) {
		//TODO
    	//PRECOND: author owns or can write this file
    	//PRECOND: username is not already in this association
		
	}

	/**
	 * @param author
	 * @param username
	 * @param calcSheet
	 */
	public void addWriter(User author, String username, CalcSheet calcSheet) {
		// TODO Auto-generated method stub
		//PRECOND: author owns or can write this file
    	//PRECOND: username MUST be able to read this file		
	}

	/**
	 * @param author
	 * @param username
	 * @param calcSheet
	 */
	public void removeReader(User author, String username, CalcSheet calcSheet) {
		// TODO Auto-generated method stub
		//PRECOND: author owns or can write this file
    	//PRECOND: username can read this file and CANNOT write this file
		
	}

	/**
	 * @param author
	 * @param username
	 * @param calcSheet
	 */
	public void removeWriter(User author, String username, CalcSheet calcSheet) {
		// TODO Auto-generated method stub
		//PRECOND: author owns or can write this file
    	//PRECOND: username can write this file.
	}
	
	/**
	 * @param calcSheet
	 */
		public void removeCalcSheet(CalcSheet calcSheet) {
			
			calcSheet.deleteAllCells();
			this.getCalcSheetSet().remove(calcSheet);
		}
	
}
