package pt.tecnico.bubbledocs.service.remote;


import java.util.Arrays;

import pt.tecnico.bubbledocs.exceptions.DuplicateEmailException;
import pt.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exceptions.InvalidEmailException;
import pt.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.tecnico.bubbledocs.exceptions.LoginException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.sdis.id.ws.*;
import pt.tecnico.sd.ClientTicket;
import pt.tecnico.sd.id.cli.*;


public class IDRemoteServices {
	
	public void createUser(String username, String email)
			throws InvalidUsernameException, DuplicateUsernameException,
			DuplicateEmailException, InvalidEmailException,
			RemoteInvocationException {
		
		try {
			
			SdIdClient idRemote = getRemote();
			idRemote.createUser(username, email);
			
		} catch(SdIdRemoteException e) {
			throw new RemoteInvocationException(e);
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
	
	public ClientTicket loginUser(String username, String password)
			throws LoginException, RemoteInvocationException {
		//TODO
		/*
		try {
			SdIdClient idRemote = getRemote();
			byte[] result = idRemote.requestAuthentication(username, password.getBytes());
			if (!Arrays.equals(result, "1".getBytes())) {
				throw new LoginException();
			}
			
		} catch (SdIdRemoteException e) {
			throw new RemoteInvocationException(e);
		} catch (AuthReqFailed_Exception e) {
			throw new LoginException(e);
		}
		*/
		
		return null;
	}
	
	public void removeUser(String username)
			throws LoginException, RemoteInvocationException {
		try {
			
			SdIdClient idRemote = getRemote();
			idRemote.removeUser(username);
			
		} catch (SdIdRemoteException e) {
			throw new RemoteInvocationException(e);
		} catch (UserDoesNotExist_Exception e) {
			throw new LoginException(e);
		} 
	}
	
	public void renewPassword(String username)
			throws LoginException, RemoteInvocationException {
		try {
			
			SdIdClient idRemote = getRemote();
			idRemote.renewPassword(username);
			
		} catch (SdIdRemoteException e) {
			throw new RemoteInvocationException(e);
		} catch (UserDoesNotExist_Exception e) {
			throw new LoginException(e);
		} 
	}
	
	private SdIdClient getRemote() throws SdIdRemoteException {
		return SdIdClient.getInstance();
	}
	
}
