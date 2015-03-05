package pt.tecnico.bubbledocs.dml;

public class User extends User_Base {
    
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
    
    /*
     * This adds another user to the list of users that can use this file.
     * The file permission can be read-only or read-write
     */
    public void addUserToCalcSheet(String username, int id, boolean readonly) {
    	//PRECOND: username exists and username != this.username
    	//PRECOND: this user owns id or can write id.
    	//TODO
    }
    
    /*
     * This removes a user that is in the list of users that can use 
     * a calcsheet.
     */
    public void removeUserToCalcSheet(String username, int id) {
    	//PRECOND: username can use this file
    	//PRECOND: this user owns id or can write id
    	//TODO
    }
    
    
    //This returns an iterator for the files id's that the user created
    public Iterable<Integer> getFilesOwned() {
    	//TODO
    	return null;
    }
    
    //This returns an iterator for the files id's this user is associated
    public Iterable<Integer> getFilesAssociated() {
    	//TODO
    	return null;
    }
    
}
