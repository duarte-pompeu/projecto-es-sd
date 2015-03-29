package pt.tecnico.bubbledocs.service;



import pt.tecnico.bubbledocs.domain.*;
// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;

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
    protected void dispatch() throws BubbleDocsException {
    	//Código para autenticação, temos de decidir o método.
    	//User user = Session.getInstance().getUser(token);
    	//TODO
    	User user = null; //Given the token, get the user.
    	user.createUser(username, name, password);    	
    }
}
