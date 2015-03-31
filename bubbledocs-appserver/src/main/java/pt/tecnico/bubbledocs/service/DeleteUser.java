package pt.tecnico.bubbledocs.service;

// add needed import declarations
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;

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
       
        if(user.getUserName() != "root"){
                throw new PermissionException("Only root can delete users.");
        }
       
        SuperUser su = new SuperUser(user.getUserName(), user.getName(), user.getPassword());
        su.deleteUser(username);
    }
    

}
