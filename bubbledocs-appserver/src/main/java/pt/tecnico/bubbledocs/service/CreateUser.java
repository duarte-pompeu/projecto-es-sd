package pt.tecnico.bubbledocs.service;



import pt.tecnico.bubbledocs.domain.*;
// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;

public class CreateUser extends SessionService {
	private String username;
	private String email;
	private String name;
	private User result;
	
    public CreateUser(String userToken, String newUsername, String email, String name) {
    	super(userToken);
    	
    	this.username = newUsername;
    	this.name = name;
    	this.email = email;
    }
    
    @Override
    protected void dispatchAfterSuperService() throws BubbleDocsException {
    	this.result = super.user.createUser(username, name, email, null);
    }
    
    public User getResult(){
    	return this.result;
    }
}