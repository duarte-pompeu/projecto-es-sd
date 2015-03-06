package pt.tecnico.bubbledocs.dml;

public class User extends User_Base {
    
	//Question. Should all _Base classes have an empty constructor?
    public User() {
    	super();
    }
	
	public User(String userName, String name, String password) {
        super();
        this.setUserName(userName);
        this.setName(name);
        this.setPassword(password);
    }
    
    /*
     * Creates a calcsheet that is associated with this user
     * as its creator. This returns the id number of the created
     * calcsheet. The creator is added to the list of read-write users
     */
    public int createCalcSheet(String name, int lines, int columns) {
    	//TODO
    	return 0;
    }
    
    
    /*
     * Gets the CalcSheet with given id. CalcSheet is readonly if this user
     * only has readonly permission. This throws a CalcSheetException if the
     * user can't read id.
     */
    CalcSheet getCalcSheet(int id) /*throws CalcSheetException*/ {
    	//TODO
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
    	//TODO
    	return null;
    }
    
}
