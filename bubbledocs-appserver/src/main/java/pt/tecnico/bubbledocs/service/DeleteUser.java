package pt.tecnico.bubbledocs.service;

// add needed import declarations
import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.User;
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
    	
    	
    	//Caso nao tenha sido accionado pelo root
    	if ( !token.toLowerCase().contains("root".toLowerCase()) ){ //que outras maneiras existem para verificar o token? 
    		throw new PermissionException("User is not allowed to create new users");
    	}
    	
    	
    	//User a ser deletado nao existe
    	
    	/*
    	
    	boolean found = false ; 
    	BubbleDocs pb = BubbleDocs.getInstance();
	 	for (User p : pb.getUserSet()) {
		 	if (p.getUserName().equals(username) ) {
		 		found = true; 
		 		break; 
		 	} 
	 	} 
	 	if (!found) {	//Username a ser deletado not found...
	 		throw new NotFoundException ("Trying to delete a user not found");
	 	}
    	*/
    
    	
    	
    }

}
