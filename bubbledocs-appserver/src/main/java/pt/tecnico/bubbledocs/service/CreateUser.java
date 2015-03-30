package pt.tecnico.bubbledocs.service;



import pt.tecnico.bubbledocs.domain.*;
// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.RepeatedIdentificatonException; 
import pt.tecnico.bubbledocs.exceptions.InvalidValueException; //Case empty username
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
    	//Código para autenticação, temos de decidir o método.
    	//User user = Session.getInstance().getUser(token);
    	//TODO
    	
    	//Se o campo username estava vazio
    	if (username.isEmpty()) {
    		throw new InvalidValueException("Username field is empty!"); 
    	}
    	
    	
    	//Caso o user root nao esteja em sessao
    	try{ 
    		User user = getSessionFromToken(token).getUser(); 
    	}
    	catch(UserNotInSessionException e){
    		throw e;
    	}
    	
    	
    	//Caso nao tenha sido accionado pelo root
    	if ( !token.toLowerCase().contains("root".toLowerCase()) ){ //que outras maneiras existem para verificar o token? 
    		throw new PermissionException("User is not allowed to create new users");
    	}
    	
    	
    	//Caso o user ja exista
    	/* TODO 
    	?
    	BubbleDocs pb = BubbleDocs.getInstance();
	 	for (User p : pb.getUserSet()) {
		 	if (p.getUserName().equals(username) ) {
		 		throw new RepeatedIdentificationException("Username already in use!"); 
		 	} 
	 	}
	 
    	*/
    	user.createUser(username, name, password);    	
    }
}
