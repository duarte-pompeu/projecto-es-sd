package pt.tecnico.bubbledocs;

import java.util.HashMap;

import pt.tecnico.bubbledocs.exceptions.LoginException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;

public class Cache {
	private HashMap<String,String> storage = new HashMap<String, String>();
	
	public boolean validate(String username, String password)
			throws LoginException {
			
		String storedPass = storage.get(username);
		if( storedPass == null){
			throw new NotFoundException("Cache: can't find values for username " + username);
		}
			
		if(storedPass.equals(password)){
			return true;
		}
		
		else return false;
	}
	
	
	public void removeFromCache(String username)
			throws NotFoundException {
		String old_pass = storage.remove(username);
		if(old_pass == null){
			throw new NotFoundException("Cache: username " + username +
					" has no stored password.");
		}
	}
	
	
	public void setNewPassword(String username, String newPassword)
		throws LoginException{
		storage.put(username, newPassword);
	}
	
	
	public boolean hasUser(String username){
		return storage.containsKey(username);
	}
}
