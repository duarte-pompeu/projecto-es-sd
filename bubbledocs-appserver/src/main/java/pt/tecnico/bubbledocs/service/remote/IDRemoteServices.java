package pt.tecnico.bubbledocs.service.remote;


import pt.tecnico.bubbledocs.exceptions.DuplicateEmailException;
import pt.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exceptions.InvalidEmailException;
import pt.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.tecnico.bubbledocs.exceptions.LoginException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.sdis.id.ws.*;
import pt.tecnico.sd.id.cli.*;


public class IDRemoteServices {
	
	public void createUser(String username, String email)
			throws InvalidUsernameException, DuplicateUsernameException,
			DuplicateEmailException, InvalidEmailException,
			RemoteInvocationException {
		
		SdIdClient idRemote = getRemote();
		
		try {
			idRemote.createUser(username, email);
		} catch(InvalidUser_Exception e) {
			throw new InvalidUsernameException(e);
		} catch(UserAlreadyExists_Exception e) {
			throw new DuplicateUsernameException(e);
		} catch(EmailAlreadyExists_Exception e) {
			throw new DuplicateEmailException(e);
		} catch(InvalidEmail_Exception e) {
			throw new InvalidEmailException(e);
		}
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
	
	private SdIdClient getRemote() throws RemoteInvocationException {
		try {
			return SdIdClient.getInstance();
		} catch (SdIdRemoteException e) {
			throw new RemoteInvocationException();
		}
	}
	
}
