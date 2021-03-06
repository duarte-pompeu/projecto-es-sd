package pt.tecnico.bubbledocs.service;

import java.io.IOException;

import org.jdom2.JDOMException;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Session;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.LoginException;

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
	 * @throws BubbleDocsException 
	 */
	@Override
	public final void dispatch() throws BubbleDocsException{
		this.user = confirmToken(token);
		try {
			this.dispatchAfterSuperService();
		} catch (IOException | JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private User confirmToken(String token){
		BubbleDocs bd = BubbleDocs.getInstance();
		Session session = bd.getSessionFromToken(token);
		return session.getUser();
	}
	
	
	protected abstract void dispatchAfterSuperService() throws BubbleDocsException, IOException, JDOMException;
}
