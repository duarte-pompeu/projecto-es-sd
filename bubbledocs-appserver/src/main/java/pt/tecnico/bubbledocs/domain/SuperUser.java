package pt.tecnico.bubbledocs.domain;

import java.util.Set;



/**
 * @author pc-w
 *
 */
public class SuperUser extends SuperUser_Base {
    
    /**
     * 
     */
    public SuperUser() {
        super();
    }
    
    /**
     * @param userName
     * @param name
     * @param password
     */
    @Deprecated
    public SuperUser(String userName, String name, String password) {
    	super();
    	init(userName, name, password);
    }
    
    public SuperUser(String userName, String name, String email, String password) {
    	super();
    	init(userName, name, email, password);
    }
    
    /* (non-Javadoc)
     * @see pt.tecnico.bubbledocs.dml.User#createUser(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    @Deprecated
    public User createUser(String userName, String name, String password) {
    	BubbleDocs bb = BubbleDocs.getInstance();
    	return bb.addUser(userName, name, password);
    }
    
    public User createUser(String userName, String name, String email, String password) {
    	BubbleDocs bb = BubbleDocs.getInstance();
    	return bb.addUser(userName, name, email, password);
    }
    
    /* (non-Javadoc)
     * @see pt.tecnico.bubbledocs.dml.User#deleteUser(java.lang.String)
     */
    @Override
    public void deleteUser(String userName) {
    	BubbleDocs bb = BubbleDocs.getInstance();
    	bb.deleteUser(userName);
    }
    
    /* (non-Javadoc)
     * @see pt.tecnico.bubbledocs.dml.User#getUserSet()
     */
    @Override
    public Set<User> getUserSet() {
    	BubbleDocs bb = BubbleDocs.getInstance();
    	return bb.getUserSet();
    }

    //please add the xml code --->>> only calcSheets are exported/imported through xml. not users!
    
}