package pt.tecnico.bubbledocs.service;



import pt.tecnico.bubbledocs.domain.*;
// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;

public class GetUserInfo extends BubbleDocsService {

	private String username;
	private User result;
	
    public GetUserInfo(String userName) {
    	this.username = userName;
    }
    
    @Override
    protected void dispatch() throws BubbleDocsException {
    	BubbleDocs bd = BubbleDocs.getInstance();
    	this.result = bd.getUser(username);
    }
    
    public User getResult(){
    	return this.result;
    }

	public String getEmail() {
		return this.result.getEmail();
	}

	public String getName() {
		return this.result.getName();
	}
}