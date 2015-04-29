package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.service.DeleteUser;

public class DeleteUserIntegrator extends BubbleDocsIntegrator {

	private DeleteUser service;
	
	public DeleteUserIntegrator(String token, String username) {
		this.service = new DeleteUser(token, username);
	}
	
	@Override
	public void execute() throws BubbleDocsException {
		service.execute();
	}

}
