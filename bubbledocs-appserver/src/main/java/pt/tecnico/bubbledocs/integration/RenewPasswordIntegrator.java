package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.service.RenewPassword;

public class RenewPasswordIntegrator extends BubbleDocsIntegrator {

	private RenewPassword service;
	
	public RenewPasswordIntegrator(String token) {
		service = new RenewPassword(token);
	}
	
	@Override
	public void execute() throws BubbleDocsException {
		service.execute();
	}

}
