package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

import pt.tecnico.bubbledocs.domain.*;
// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;

public class CreateUserIntegrator extends BubbleDocsIntegrator {

	private String token;
	private String username;
	private String email;
	private String name;
	private User result;
	
    public CreateUserIntegrator(String userToken, String newUsername,
            String email, String name) {
    	this.token = userToken;
    	this.username = newUsername;
    	this.name = name;
    	this.email = email;
    }
    
    @Override
    protected void dispatch() throws BubbleDocsException {
    	User user = getUserFromToken(token);
    	this.result = user.createUser(username, name, email, null);
		
		try { 
			IDRemoteServices remote = new IDRemoteServices();
			remote.createUser(username, email);
		}
		catch(BubbleDocsException e) {
			user.deleteUser(username);
			this.result = null;
			throw e;
		}
    }
    
    public User getResult(){
    	return this.result;
    }
}