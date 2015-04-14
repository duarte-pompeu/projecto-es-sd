package pt.tecnico.bubbledocs.service;



import pt.tecnico.bubbledocs.domain.*;
// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.InvalidValueException; //Case empty username
import pt.tecnico.bubbledocs.exceptions.RepeatedIdentificationException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException; //incorrect Cell or Reference given
import pt.tecnico.bubbledocs.exceptions.PermissionException; //User doesnt have create permissions

public class CreateUser extends BubbleDocsService {

	private String token;
	private String username;
	private String name;
	private String password;
	
    public CreateUser(String userToken, String newUsername,
            String password, String name) {
    	this.token = userToken;
    	this.username = newUsername;
    	this.name = name;
    	this.password = password;
    }
    
    @Override
    protected void dispatch() throws BubbleDocsException, RepeatedIdentificationException, InvalidValueException,UserNotInSessionException, PermissionException {
    	User user = this.getSessionFromToken(token).getUser();
    	user.createUser(username, name, password);    	
    }
}