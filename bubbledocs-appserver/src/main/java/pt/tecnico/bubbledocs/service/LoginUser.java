package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.LoginException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class LoginUser extends BubbleDocsService {

    private String userToken;
    private String username;
    private String password;

    public LoginUser(String username, String password) {
    	this.username = username;
    	this.password = password;
    }

    @Override
    protected void dispatch() throws LoginException {
    	BubbleDocs bd = BubbleDocs.getInstance();
    	User user;
    	
    	bd.refreshSessions();
		
    	// must be able to get user
    	try {
    		user = bd.getUser(username); //search for a valid user
    	}
    	catch (NotFoundException e){
    		throw new LoginException("invalid password or username");
    	}
    	
    	// try remote login
    	try{
    		IDRemoteServices remote = new IDRemoteServices();
    		remote.loginUser(username, password);
    	}
    	
    	// good connection but bad login input
    	catch(LoginException e){
    		throw e;
    	}
    	
    	// connection failed, use local session
    	catch(RemoteInvocationException e){
    		
    		/* code from earlier delivery
    		
    		if (!user.getPassword().equals(password)) //correct password?
    			throw new LoginException("Login User Service: Invalid password");
    	
    		*/
    		if(!user.getPassword().equals(password)){
    			throw new  UnavailableServiceException("Can't login: fail on both remote and local login."); 
    		}
    	}
    	
		userToken = bd.addSession(user);   	
    }

    public final String getUserToken() {
	return userToken;
    }
    
    public String getResult(){
    	return getUserToken();
    }
}
