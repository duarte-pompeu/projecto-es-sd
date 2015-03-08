package pt.tecnico.bubbledocs.dml;
import org.jdom2.Element;
import pt.tecnico.bubbledocs.exceptions.PermissionException;
import java.util.*;

public class User extends User_Base {
    

    public User() {
    	super();
    }
	
	public User(String userName, String name, String password) {
        super();
        init(userName, name, password);
        //this.ownedFilenames = new HashSet<String>();
        //this.allFiles = new HashMap<Integer, Boolean>();
    }
	
	protected void init(String userName, String name, String password) {
		this.setUserName(userName);
        this.setName(name);
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
    	return sheet;
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

    
    public User createUser(String userName, String name, String password) {
    	throw new PermissionException("You don't have permission to do this action");
    }

    public void deleteUser(String userName) {
    	throw new PermissionException("You don't have permission to do this action");
    }
    
    public Set<User> getUserSet() {
    	throw new PermissionException("You don't have permission to do this action");
    }

}
