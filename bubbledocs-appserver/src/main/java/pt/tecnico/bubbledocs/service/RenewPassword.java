package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RenewPassword extends BubbleDocsService {

	private String token;

	public RenewPassword(String token) {
		this.token = token;
	}

	//The only thing atomic about this service is clearing the password
	//There could be a login going on or something and the SD-ID server
	//happened to poofed its login micro-service and it's checking the
	//locally stored password.
	@Override
	protected void dispatch() throws BubbleDocsException {		
		User user = getUserFromToken(token);
		user.clearPassword();
	}
	
	//What is this doing here? This is a non atomic method
	//that invokes a remote service. The invocation logic
	//is in User.
	public void renewPassword(IDRemoteServices remote) {
		User user = getUserFromToken(token);
		user.renewPassword(remote);
	}

}
