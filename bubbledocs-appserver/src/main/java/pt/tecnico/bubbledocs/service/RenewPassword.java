package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.*;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RenewPassword extends BubbleDocsService {

	private String token;
	private String oldPass;
	private User user;

	public RenewPassword(String token) {
		this.token = token;
	}

	//The only thing atomic about this service is clearing the password
	//There could be a login going on or something and the SD-ID server
	//happened to poofed its login micro-service and it's checking the
	//locally stored password.
	@Override
	protected void dispatch() throws BubbleDocsException {		
		this.user = getUserFromToken(token);
		this.oldPass = user.getPassword();
		user.clearPassword();
	}

	public void compensate() {
		user.setPassword(oldPass);		
	}
}
