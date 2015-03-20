package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.dml.BubbleDocs;
import pt.tecnico.bubbledocs.dml.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.LoginException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;

public class LoginUser extends BubbleDocsService {

    private String userToken;
    private String username;
    private String password;

    public LoginUser(String username, String password) {
    	this.username = username;
    	this.password = password;
    }

    @Override
    protected void dispatch() throws BubbleDocsException {
    	try {
    		BubbleDocs bd = BubbleDocs.getInstance();
    		
    		bd.refreshSessions();
    		
    		User user = bd.getUser(username); //search for a valid user
    		
    		if (!user.getPassword().equals(password)) //correct password?
    			throw new LoginException("invalid password or username");
    		
    		userToken = bd.addSession(user);   		
    	
    	} catch (NotFoundException e) {
    		throw new LoginException("invalid password or username");
    	}    	
    }

    public final String getUserToken() {
	return userToken;
    }
}
