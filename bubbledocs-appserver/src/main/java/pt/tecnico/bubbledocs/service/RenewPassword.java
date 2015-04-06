package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RenewPassword extends BubbleDocsService {

	private String token;

	public RenewPassword(String token) {
		this.token = token;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		IDRemoteServices remote = new IDRemoteServices();
		User user = getSessionFromToken(token).getUser();
		String username = user.getUserName();
		
		try {
			remote.renewPassword(username);
		} catch (RemoteInvocationException e) {
			throw new UnavailableServiceException();
		}
		
		//Stored password must be removed.
		user.setPassword(null);
	}

}
