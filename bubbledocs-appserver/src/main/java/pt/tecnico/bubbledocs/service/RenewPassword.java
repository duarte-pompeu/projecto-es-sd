package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;

public class RenewPassword extends BubbleDocsService {

	private String token;

	public RenewPassword(String token) {
		this.token = token;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		//TODO
	}

}