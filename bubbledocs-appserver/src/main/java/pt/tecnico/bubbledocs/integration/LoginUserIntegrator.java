package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.LoginException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.LoginUser;

public class LoginUserIntegrator extends BubbleDocsIntegrator {

	private LoginUser service;
	
	public LoginUserIntegrator(String username, String password) {
		service = new LoginUser(username, password);
	}
	
	@Override
	public void execute() throws BubbleDocsException {
		try {
			getIDRemoteServices().loginUser(service.getUsername(), service.getPassword());
			service.successfulRemoteLogin();
		} catch (RemoteInvocationException e) {
			try {
				service.execute();
			} catch (LoginException f) {
				throw new UnavailableServiceException();
			}
		}
	}
	
	public String getResult(){
		return service.getResult();
	}

}
