package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exceptions.LoginException;

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
    	
		userToken = bd.login(username, password);   	
    }

    public final String getUserToken() {
	return userToken;
    }
    
    public String getResult(){
    	return getUserToken();
    }
}
