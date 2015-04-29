package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.GetUsername4Token;
import pt.tecnico.bubbledocs.service.RenewPassword;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RenewPasswordIntegrator extends BubbleDocsIntegrator {

	private RenewPassword service;
	private String token;
	
	public RenewPasswordIntegrator(String token) {
		this.token = token;
		this.service = new RenewPassword(token);
	}
	
	@Override
	public void execute() throws BubbleDocsException {
		GetUsername4Token u4t = new GetUsername4Token(this.token);
		u4t.execute();
		
		service.execute();
		
		try {
			getIDRemoteServices().renewPassword(u4t.getResult());
		} catch (RemoteInvocationException e) {
			service.compensate();
			throw new UnavailableServiceException(e);
		} 
	}

}
