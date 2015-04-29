package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.*;
import pt.tecnico.bubbledocs.service.CreateUser;
import pt.tecnico.bubbledocs.service.DeleteUser;
import pt.tecnico.bubbledocs.service.GetUserInfo;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class DeleteUserIntegrator extends BubbleDocsIntegrator {

	private DeleteUser service;
	private IDRemoteServices remote;
	private String token;
	private String username;
	private String email;
	private String name;
	
	public DeleteUserIntegrator(String token, String username) {
		this.service = new DeleteUser(token, username);
		this.remote = new IDRemoteServices();
		this.token = token;
		this.username = username;
		
		GetUserInfo info = new GetUserInfo(username);
		info.execute();

		this.email = info.getEmail();
		this.name  = info.getName();
	}
	
	@Override
	public void execute() throws BubbleDocsException {
		service.execute();
		
		try {
			remote.removeUser(username);
		} catch (RemoteInvocationException e) {
			new CreateUser(token, username, this.email, this.name).execute();
			throw new UnavailableServiceException(e);
		}
	}

}
