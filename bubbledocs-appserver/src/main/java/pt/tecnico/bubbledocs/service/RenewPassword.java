package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;

public class RenewPassword extends SessionService {

	private String oldPass;

	public RenewPassword(String token) {
		super(token);
	}

	//The only thing atomic about this service is clearing the password
	//There could be a login going on or something and the SD-ID server
	//happened to poofed its login micro-service and it's checking the
	//locally stored password.
	@Override
	protected void doAfterSuperService() throws BubbleDocsException {
		this.oldPass = user.getPassword();
		user.clearPassword();
	}

	public void compensate() {
		user.setPassword(oldPass);		
	}
}
