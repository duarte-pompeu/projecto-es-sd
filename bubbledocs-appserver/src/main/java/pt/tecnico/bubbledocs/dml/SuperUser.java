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
    	//TODO
    	return null;
    }
    
    @Override
    public void deleteUser(String userName) {
    	//TODO
    }
    
    @Override
    public Set<User> getUserSet() {
    	//TODO
    	return null;
    }

    //please add the xml code
    
}
