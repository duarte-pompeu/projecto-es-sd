package pt.tecnico.bubbledocs.integration;


// add needed import declarations
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Session;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;

public abstract class BubbleDocsIntegrator {

    public abstract void execute() throws BubbleDocsException;
    
    //Almost every service depends on an active session.
    protected Session getSessionFromToken(String token) throws UserNotInSessionException {
    	Session session = BubbleDocs.getInstance().getSessionFromToken(token); 
    	session.touch();
    	return session;
    }
    
    protected User getUserFromToken(String token) throws UserNotInSessionException {
    	return getSessionFromToken(token).getUser();
    }
    
    protected final IDRemoteServices getIDRemoteServices() {
    	return new IDRemoteServices();
    }
    
    protected final StoreRemoteServices getStoreRemoteServices() {
		return new StoreRemoteServices();
    }
}