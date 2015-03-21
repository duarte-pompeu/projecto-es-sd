package pt.tecnico.bubbledocs.service;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.dml.Session;
// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.dml.BubbleDocs;

public abstract class BubbleDocsService {

    @Atomic
    public final void execute() throws BubbleDocsException {
        dispatch();
    }

    protected abstract void dispatch() throws BubbleDocsException;
    
    //Almost every service depends on an active session.
    protected Session getSessionFromToken(String token) throws UserNotInSessionException {
    	Session session = BubbleDocs.getInstance().getSessionFromToken(token); 
    	session.touch();
    	return session;
    }
}
