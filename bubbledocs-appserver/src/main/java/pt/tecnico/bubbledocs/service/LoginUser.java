package pt.tecnico.bubbledocs.service;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exceptions.LoginException;

public class LoginUser extends BubbleDocsService {

	private String userToken;
	private String username;
	private String password;

	public LoginUser(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	protected void dispatch() throws LoginException {
		userToken = BubbleDocs.getInstance().login(username, password);   	
	}

	@Atomic
	public void successfulRemoteLogin() {
		BubbleDocs.getInstance().getUser(username).setPassword(password);
		dispatch();
	}

	public final String getResult() {
		return userToken;
	}
	
	
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
}
