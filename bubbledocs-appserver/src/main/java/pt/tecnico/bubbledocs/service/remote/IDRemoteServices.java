package pt.tecnico.bubbledocs.service.remote;


import pt.tecnico.bubbledocs.exceptions.DuplicateEmailException;
import pt.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exceptions.InvalidEmailException;
import pt.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.tecnico.bubbledocs.exceptions.LoginException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;


public class IDRemoteServices {
	public void createUser(String username, String email)
			throws InvalidUsernameException, DuplicateUsernameException,
			DuplicateEmailException, InvalidEmailException,
			RemoteInvocationException {
		// TODO : the connection and invocation of the remote service
	}
	public void loginUser(String username, String password)
			throws LoginException, RemoteInvocationException {
		// TODO : the connection and invocation of the remote service
	}
	public void removeUser(String username)
			throws LoginException, RemoteInvocationException {
		// TODO : the connection and invocation of the remote service
	}
	public void renewPassword(String username)
			throws LoginException, RemoteInvocationException {
		// TODO : the connection and invocation of the remote service
	}
}
