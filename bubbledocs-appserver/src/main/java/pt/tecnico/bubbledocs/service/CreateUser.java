package pt.tecnico.bubbledocs.service;



import pt.tecnico.bubbledocs.domain.*;
// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.exceptions.PermissionException;

public class CreateUser extends BubbleDocsService {

	private String token;
	private String username;
	private String name;
	private String password;
	private String email;
	
    public CreateUser(String userToken, String newUsername, String email, String name) {
    	this.token = userToken;
    	this.username = newUsername;
    	this.name = name;
    	this.password = null;
    	this.email = email;
    }
    
    @Override
    protected void dispatch() throws BubbleDocsException {
    	User user = this.getSessionFromToken(token).getUser();
    	
		
    	try {
			IDRemoteServices remote = new IDRemoteServices();
			remote.createUser(username, email);
		} catch (RemoteInvocationException e) {
			throw new UnavailableServiceException();
		}
   
	try{
	  user.createUser(username, name, email, "randompass");
    	}
	catch (PermissionException e) {
	  throw new PermissionException("You have no permission to create an user");
	  
	}   	
    }
}
