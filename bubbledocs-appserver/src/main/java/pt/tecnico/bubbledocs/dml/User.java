package pt.tecnico.bubbledocs.dml;
import org.jdom2.Element;

import java.util.*;

public class User extends User_Base {
    

    public User() {
    	super();
    }
	
	public User(String userName, String name, String password) {
        super();
        this.setUserName(userName);
        this.setName(name);
        this.setPassword(password);
        //this.ownedFilenames = new HashSet<String>();
        //this.allFiles = new HashMap<Integer, Boolean>();
    }
    
    /*
     * Creates a calcsheet that is associated with this user
     * as its creator. This returns the id number of the created
     * calcsheet. The creator is added to the list of read-write users
     */
    public int createCalcSheet(String name, int lines, int columns) {
    	CalcSheet sheet = new CalcSheet(name, lines, columns);
    	this.addCreatedCalcSheet(sheet);
    	this.addReadableCalcSheet(sheet);
    	this.addWriteableCalcSheet(sheet);
    	return sheet.getId();
    }
    
    
    /*
     * Gets the CalcSheet with given id. CalcSheet is readonly if this user
     * only has readonly permission. This throws a PermissionException if the
     * user can't read id.
     */
    CalcSheet getCalcSheet(int id) /*throws CalcSheetException*/ {
    	//TODO
    	/*
    	//Throw CalcSheetNotFoundException
    	CalcSheet sheet = BubbleDocs.getInstance().getCalcSheet(id);
    	
    	if (!this.getReadableCalcSheetSet().contains(sheet) {
    	  throw new PermissionException();
    	}
    	
    	if (!this.getWriteableCalcSheetSet().contains(sheet)) {
    	  sheet.setReadOnly();
    	}
    	
    	return sheet;
    	
    	*/    	
    	return null;
    }
        
    /*
     * Deletes the calcsheet with the given id, if it's owned by
     * this user.
     */
    //Este método deve ser incluído?
    public void deleteCalcSheet(int id) {
    	//PRECOND: this owns id
    	//TODO
    }    
    
    //For now I'm simplifying, this method could have splited versions
    //This returns an iterator for the files id's that the user created, the user can read 
    //and the user can read-write
    public Iterable<CalcSheet> getAllFiles() {    	
    	//OMG java lambdas!!!!!!11
    	return () -> {
    		return new Iterator<CalcSheet>() {
    			Iterator<CalcSheet> readables = 
    					User.this.getReadableCalcSheetSet().iterator();
    			@Override
    			public boolean hasNext() {
    				return readables.hasNext();
    			}

    			@Override
    			public CalcSheet next() {
    				CalcSheet next = readables.next();
    				if (!User.this.getWriteableCalcSheetSet().contains(next)) {
    					next.setReadonly();
    				}
    				return next;
    			}

    		};
    	};
    }



    public void importFromXML(Element userElement) {
    	this.setName(userElement.getAttribute("name").getValue());
    	this.setUserName(userElement.getAttribute("userName").getValue());
    	this.setPassword(userElement.getAttribute("password").getValue());
    }

    public Element exportToXML() {
    	Element element = new Element("user");
    	element.setAttribute("name", this.getName());
    	element.setAttribute("userName", this.getUserName());
    	element.setAttribute("password", this.getPassword());

    	return element;
    }



}
