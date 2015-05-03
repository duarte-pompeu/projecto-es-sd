package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.domain.*;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.CreateUser;
import pt.tecnico.bubbledocs.service.DeleteUser;

public class CreateUserIntegrator extends BubbleDocsIntegrator {

	private CreateUser service;
	private IDRemoteServices remote;
	private String token;
	private String username;
	private String email;
	private String name;
	private User result;
	
    public CreateUserIntegrator(String userToken, String newUsername,
            String email, String name) {
    	this.token = userToken;
    	this.username = newUsername;
    	this.email = email;
		this.name = name;
		this.service = new CreateUser(token, username, email, name);
		this.remote = new IDRemoteServices();
    }
    
    @Override
    public void execute() throws BubbleDocsException {
    	service.execute();
		this.result = service.getResult();
		
		try {
			remote.createUser(username, email);
		} catch (RemoteInvocationException e) {
			DeleteUser DelUser = new DeleteUser(token,username);
			DelUser.execute();
			this.result = null;
			throw new UnavailableServiceException(e);
		}
	}
    
    public User getResult() {
    	return this.result;
    }
}
	
	
