package pt.tecnico.bubbledocs.dml;

import java.util.Set;

public class SuperUser extends SuperUser_Base {
    
    public SuperUser() {
        super();
    }
    
    public SuperUser(String userName, String name, String password) {
    	super();
    	init(userName, name, password);
    }
    
    @Override
    public User createUser(String userName, String name, String password) {
    	BubbleDocs bb = BubbleDocs.getInstance();
    	return bb.addUser(userName, name, password);
    }
    
    @Override
    public void deleteUser(String userName) {
    	BubbleDocs bb = BubbleDocs.getInstance();
    	bb.deleteUser(userName);
    }
    
    @Override
    public Set<User> getUserSet() {
    	BubbleDocs bb = BubbleDocs.getInstance();
    	return bb.getUserSet();
    }

    //please add the xml code
    
}
