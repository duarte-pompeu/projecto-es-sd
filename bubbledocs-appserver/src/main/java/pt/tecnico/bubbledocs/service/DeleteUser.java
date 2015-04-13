package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class DeleteUser extends BubbleDocsService {
	private String token;
	private String username; 
	
    	public DeleteUser(String userToken, String toDeleteUsername) {
	token = userToken;
	username = toDeleteUsername; 
    }

    @Override
    protected void dispatch() throws BubbleDocsException {
    	User user = this.getSessionFromToken(token).getUser();
       
    	try{
    		IDRemoteServices id_service = new IDRemoteServices();
    		id_service.removeUser(username);
    	}
    	 
    	catch(BubbleDocsException e){
    		throw e;
    	}
    
        user.deleteUser(username);
    }
    

}
