package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;

public class DeleteUser extends SessionService {
	private String username; 

	public DeleteUser(String token, String username) {
		super(token);
		
		this.token = token;
		this.username = username; 
	}

	@Override
	protected void doAfterSuperService() throws BubbleDocsException {
		super.user.deleteUser(username);
	}
}
