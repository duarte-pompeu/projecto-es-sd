package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.*;
// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;

public class GetUsername4Token extends BubbleDocsService {

	private String token;
	private String result;
	
    public GetUsername4Token(String token) {
    	this.token = token;
    }
    
    @Override
    protected void dispatch() throws BubbleDocsException {
    	User user = getUserFromToken(token);
    	this.result = user.getUserName();
    }
    
    public String getResult(){
    	return this.result;
    }
}