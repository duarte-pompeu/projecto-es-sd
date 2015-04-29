package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;

public class DeleteUser extends BubbleDocsService {
	private String token;
	private String username; 

	public DeleteUser(String token, String username) {
		this.token = token;
		this.username = username; 
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		User user = getUserFromToken(token);

		user.deleteUser(username);
	}
}
