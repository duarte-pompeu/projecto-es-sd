/**
 * 
 */
package pt.tecnico.sd.id;

import java.util.Map;
import java.util.HashMap;

import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

/**
 * 
 * This is a table that stores users and can index user by username and email
 * 
 * @author danix
 *
 */
public class UserTable {
	private Map<String, User> usernameMap;
	private Map<String, User> emailMap;

	public UserTable() {
		usernameMap = new HashMap<String, User>();
		emailMap = new HashMap<String, User>();
	}

	/**
	 * Adds a user to the user table.
	 * If there is a user with the same username or email it throws
	 * UserAlreadyExists_Exception or EmailAlreadyExists_Exception respectively
	 * 
	 * @param username
	 * @param email
	 * @param password
	 * @throws UserAlreadyExists_Exception there is a user with the same username
	 * @throws EmailAlreadyExists_Exception there is a user with the same email
	 */
	public void addUser(String username, String email, byte[] password) 
			throws UserAlreadyExists_Exception, EmailAlreadyExists_Exception {
		//FIXME Which exception should be thrown first?
		throwIfEmailAlreadyExists(email);
		throwIfUserAlreadyExists(username);

		User user = new User(username, email, password);
		usernameMap.put(username, user);
		emailMap.put(email, user);  
	}





	/**
	 * Removes the user with given username from the table.
	 * If there is no such user with the given username, this throws UserDoesNotExist_Exception.
	 * 
	 * @param username
	 * @throws UserDoesNotExist_Exception there is no user with the given username
	 */
	public void removeUser(String username) throws UserDoesNotExist_Exception {
		throwIfUserDoesNotExist(username);
		User user = usernameMap.remove(username);
		emailMap.remove(user.email);
	}


	/**
	 * Changes the password of the user with the given username.
	 * If there is no such user with the given username, this throws UserDoesNotExist_Exception.
	 * 
	 * @param username
	 * @param newPassword
	 * @throws UserDoesNotExist_Exception there is no user with the given username
	 */
	public void changePassword(String username, byte[] newPassword) throws UserDoesNotExist_Exception {
		throwIfUserDoesNotExist(username);
		User user = usernameMap.get(username);
		user.password = newPassword;
	}
	
	//Returns a User - to be used in tests. Returns null if the user does not exist.
	protected User getUserByUsername(String username) {
		return this.usernameMap.get(username);
	}

	/**
	 * Returns the password of the user with given username. 
	 * If there is no such user, returns null.
	 * 
	 * @param username
	 * @return username's password, or null if username does not exist.
	 */
	public byte[] getPassword(String username) {
		User user = usernameMap.get(username);
		if (user == null) return null;
		return user.password;
	}



	private void throwIfUserDoesNotExist(String username) throws UserDoesNotExist_Exception {
		if (!usernameMap.containsKey(username)) {
			UserDoesNotExist fault = new UserDoesNotExist();
			fault.setUserId(username);
			throw new UserDoesNotExist_Exception(username + " does not exist", fault);
		}
	}

	private void throwIfEmailAlreadyExists(String email)
			throws EmailAlreadyExists_Exception {
		if (emailMap.containsKey(email)) {
			EmailAlreadyExists fault = new EmailAlreadyExists();
			fault.setEmailAddress(email);
			throw new EmailAlreadyExists_Exception(email + " is already being used", fault);
		}
	}

	private void throwIfUserAlreadyExists(String username)
			throws UserAlreadyExists_Exception {
		if (usernameMap.containsKey(username)) {
			UserAlreadyExists fault = new UserAlreadyExists();
			fault.setUserId(username);
			throw new UserAlreadyExists_Exception(username + " is unavailable", fault);
		}
	}
}


class User {
	String username;
	String email;
	byte[] password;
	
	User(String username, String email, byte[] password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}
}