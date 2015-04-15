package pt.tecnico.bubbledocs.service;



import pt.tecnico.bubbledocs.domain.*;
// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.PermissionException; //User doesnt have create permissions
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class CreateUser extends BubbleDocsService {

	private String token;
	private String username;
	private String email;
	private String name;
	private String password;
	private User result;
	
    public CreateUser(String userToken, String newUsername,
            String email, String name) {
    	this.token = userToken;
    	this.username = newUsername;
    	this.name = name;
    	this.email = email;
    	this.password = null;
    }
    
    @Override
    protected void dispatch() throws BubbleDocsException {
    	User user = this.getSessionFromToken(token).getUser();
    	this.result = user.createUser(username, name, email, null);
    }
    
    public User getResult(){
    	return this.result;
    }
}