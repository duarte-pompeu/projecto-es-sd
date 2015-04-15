package pt.tecnico.bubbledocs.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import pt.tecnico.bubbledocs.exceptions.PermissionException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

/**
 * @author pc-w
 *
 */
public class User extends User_Base { 

    /**
     * 
     */
    public User() {
    	super();
    }
	
	/**
	 * @param userName
	 * @param name
	 * @param password
	 */
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
	
	/**
	 * @param userName
	 * @param name
	 * @param password
	 */
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
    /**
     * @param name
     * @param lines
     * @param columns
     * @return
     */
    public CalcSheet createCalcSheet(String name, int lines, int columns) {
    	CalcSheet sheet = new CalcSheet(name, lines, columns);
    	this.addCreatedCalcSheet(sheet);
    	this.addReadableCalcSheet(sheet);
    	this.addWriteableCalcSheet(sheet);
    	return sheet;
    }
    
    
    //For now I'm simplifying, this method could have splited versions
    //This returns an iterator for the files id's that the user created, the user can read 
    //and the user can read-write
    /**
     * @return
     */
    public Iterable<CalcSheet> getAllFiles() {    	
    	Set<CalcSheet> set = new HashSet<CalcSheet>(this.getReadableCalcSheetSet());
    	set.addAll(this.getCreatedCalcSheetSet());
    	return set;
    }

    
    /**
     * @param userName
     * @param name
     * @param password
     * @return
     */
    @Deprecated
    public User createUser(String userName, String name, String password) {
    	throw new PermissionException("You don't have permission to do this action");
    }
    
    public User createUser(String userName, String name, String email, String password) {
    	throw new PermissionException("You don't have permission to do this action");
    }

    /**
     * @param userName
     */
  
    public void deleteUser(String userName) {
    	throw new PermissionException("You don't have permission to do this action");
    }
    
    /**
     * @return
     */
    public Set<User> getUserSet() {
    	throw new PermissionException("You don't have permission to do this action");
    }
    
    public boolean canRead(CalcSheet sheet) {
    	return this.getReadableCalcSheetSet().contains(sheet);
    }
    
    public boolean canWrite(CalcSheet sheet) {
    	return this.getWriteableCalcSheetSet().contains(sheet);
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

	public void renewPassword() {
		String username = getUserName();
		
		try {
			IDRemoteServices remote = new IDRemoteServices();
			remote.renewPassword(username);
		} catch (RemoteInvocationException e) {
			throw new UnavailableServiceException();
		}
		
		//Stored password must be removed.
		setPassword(null);
	}
}
