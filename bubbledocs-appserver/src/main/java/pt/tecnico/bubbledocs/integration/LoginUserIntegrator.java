package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.service.LoginUser;

public class LoginUserIntegrator extends BubbleDocsIntegrator {

	private LoginUser service;
	
	public LoginUserIntegrator(String username, String password) {
		service = new LoginUser(username, password);
	}
	
	@Override
	public void execute() throws BubbleDocsException {
		service.execute();
	}
	
	public String getResult(){
		return service.getResult();
	}

}
