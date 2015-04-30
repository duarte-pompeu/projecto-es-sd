package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Session;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;

public abstract class SessionService extends BubbleDocsService {
	String token;
	User user;
	
	
	public SessionService(String userToken){
		this.token = userToken;
	}
	
	/**
	 * Dispatch is final because we dont want subclasses to override the session check.
	 * Override afterSuperAction() instead.
	 * 
	 * Good reason to remove final from dispatch: another abstract service (such as permission checking after token checking).
	 * Don't screw it up though, remember to always do super.confirmToken() or similar.
	 */
	@Override
	public final void dispatch(){
		this.user = confirmToken(token);
		this.afterSuperAction();
	}
	
	
	private User confirmToken(String token){
		BubbleDocs bd = BubbleDocs.getInstance();
		Session session = bd.getSessionFromToken(token);
		
		if(session == null){
			//throw smth, although it seems getSession already throws
		}
		
		return session.getUser();
	}
	
	
	protected abstract void afterSuperAction() throws BubbleDocsException;
}
