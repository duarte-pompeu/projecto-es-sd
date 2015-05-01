package pt.tecnico.bubbledocs.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import pt.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;

/**
 * @author pc-w
 *
 */
public class User extends User_Base { 
	private static final int USERNAME_MIN_LEN = 3;
	private static final int USERNAME_MAX_LEN = 8;
    
    public User() {
    	super();
    }
	
    @Deprecated
	public User(String userName, String name, String password) {
        super();
        init(userName, name, password);
        //this.ownedFilenames = new HashSet<String>();
        //this.allFiles = new HashMap<Integer, Boolean>();
    }
    
    public User(String userName, String name, String email, String password) {
    	super();
    	init(userName, name, email, password);
    }
	

    @Deprecated
	protected void init(String userName, String name, String password) {
	    this.setUserName(userName);
        this.setName(name);
        this.setEmail(null);
        this.setPassword(password);
	}
    
    protected void init(String userName, String name, String email, String password) {
    	this.setUserName(userName);
        this.setName(name);
        this.setEmail(email);
        this.setPassword(password);
    }
    
    /*
     * Creates a calcsheet that is associated with this user
     * as its creator. This returns the id number of the created
     * calcsheet. The creator is added to the list of read-write users
     */
    public CalcSheet createCalcSheet(String name, int lines, int columns) {
    	CalcSheet sheet = new CalcSheet(name, lines, columns);
    	this.addCreatedCalcSheet(sheet);
    	this.addReadableCalcSheet(sheet);
    	this.addWriteableCalcSheet(sheet);
    	BubbleDocs.getInstance().addCalcSheet(sheet);
    	return sheet;
    }
    
    @Override
    public void setUserName(String username){
    	//business constraint: length of username is restricted
		if(username.length() < USERNAME_MIN_LEN){
			throw new InvalidUsernameException("Username " + username + " is too short. "
					+ "Minimum length is " + USERNAME_MIN_LEN + ".");
		}
		if(username.length() > USERNAME_MAX_LEN){
			throw new InvalidUsernameException("Username " + username + " is too long. "
					+ "Maxmium length is " + USERNAME_MAX_LEN + ".");
		}
		
		super.setUserName(username);
    }
    
    
    //For now I'm simplifying, this method could have splited versions
    //This returns an iterator for the files id's that the user created, the user can read 
    //and the user can read-write
    public Iterable<CalcSheet> getAllFiles() {    	
    	Set<CalcSheet> set = new HashSet<CalcSheet>(this.getReadableCalcSheetSet());
    	set.addAll(this.getCreatedCalcSheetSet());
    	return set;
    }

    
    @Deprecated
    public User createUser(String userName, String name, String password) {
    	throw new PermissionException("You don't have permission to do this action");
    }
    
    public User createUser(String userName, String name, String email, String password) {
    	throw new PermissionException("You don't have permission to do this action");
    }

  
    public void deleteUser(String userName) {
    	throw new PermissionException("You don't have permission to do this action");
    }
    
    public Set<User> getUserSet() {
    	throw new PermissionException("You don't have permission to do this action");
    }
    
    public boolean canRead(CalcSheet sheet) {
    	return this.getReadableCalcSheetSet().contains(sheet);
    }
    
    public boolean canWrite(CalcSheet sheet) {
    	return this.getWriteableCalcSheetSet().contains(sheet) ||
    		   this.getCreatedCalcSheetSet().contains(sheet);
    }
    
    public boolean owns(CalcSheet sheet) {
    	return this.getCreatedCalcSheetSet().contains(sheet);
    }

    
  	Iterable<CalcSheet> getSheets(String substring){
  		ArrayList<CalcSheet> csList = new ArrayList<CalcSheet>();
  		
  		for(CalcSheet cs: this.getCreatedCalcSheetSet()){
  			if(cs.getName().contains(substring)){
  				csList.add(cs);
  			}
  		}
  		
  		return csList;
  	}

	public void delete() {
		deleteDomainObject();
	}


	public void clearPassword() {
		setPassword(null);
	}
}
