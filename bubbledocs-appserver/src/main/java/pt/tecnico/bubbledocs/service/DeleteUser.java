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
    	BubbleDocs pb = BubbleDocs.getInstance();
	
	
	//Caso o user que chama nao exista
	
	/* // metodo suspeito: 
	boolean found = false; 
	BubbleDocs pb = BubbleDocs.getInstance();
	 	for (User p : pb.getUserSet()) {
		 	if (p.getUserName().equals(username) ) {
		 		found = true ;
		 		break; 
		 	} 
	 	}
	 	
	 if (!found) { 
	 	throw new NotFoundException ("User trying to delete does not exist") ; 
	 }
	 
	 */
	//Caso o user que chama nao esteja em sessao
    	try{ 
    		User user = getSessionFromToken(token).getUser(); 
    	}
    	catch(UserNotInSessionException e){
    		throw e;
    	}

    	
    	
    	//User a ser removido nao existe
    	
    	
	 	for (User p : pb.getUserSet()) 
		 	if (!p.getUserName().equals(username) ) 
	 		throw new NotFoundException ("Trying to delete a user not found");
	 		
    }

}
