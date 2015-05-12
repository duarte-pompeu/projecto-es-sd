package pt.tecnico.bubbledocs.service.remote;

import org.junit.Test;

import pt.tecnico.bubbledocs.exceptions.DuplicateEmailException;
import pt.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exceptions.InvalidEmailException;
import pt.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.tecnico.bubbledocs.exceptions.LoginException;

public class IDRemoteServicesRemoteTest {
	
	private static String NEW_USERNAME = "guilherme";
	private static String NEW_EMAIL = "guilherme@example.com";
	private static String ANY_USERNAME = "fausto";
	private static String ANY_EMAIL = "fausto@example.com";
	private static String WRONG_PASSWORD = "Ah ah ah, you didn't say the magic word!";
	private static String EXISTING_USERNAME = "alice";
	private static String EXISTING_PASSWORD = "Aaa1";
	private static String EXISTING_EMAIL = "alice@tecnico.pt";
	private static String DELETING_USERNAME = "carla";
	private static String RENEWING_USERNAME = "bruno";

	
	//--CREATE USER--//
	
	@Test
	public void testCreateUser() {
		new IDRemoteServices().createUser(NEW_USERNAME, NEW_EMAIL);
	}
	
	@Test(expected=InvalidUsernameException.class)
	public void testCreateInvalidUser1() throws Exception {
		new IDRemoteServices().createUser("", ANY_EMAIL);
	}
	
	@Test(expected=InvalidUsernameException.class)
	public void testCreateInvalidUser2() throws Exception {
		new IDRemoteServices().createUser(null, ANY_EMAIL);
	}
	
	@Test(expected=InvalidEmailException.class)
	public void testCreateInvalidEmail1() throws Exception {
		new IDRemoteServices().createUser(ANY_USERNAME, "");
	}
	
	@Test(expected=InvalidEmailException.class)
	public void testCreateInvalidEmail2() throws Exception {
		new IDRemoteServices().createUser(ANY_USERNAME, null);
	}
	
	@Test(expected=DuplicateUsernameException.class)
	public void testCreateExistingUser() throws Exception {		
		new IDRemoteServices().createUser(EXISTING_USERNAME, ANY_EMAIL);
	}
	
	@Test(expected=DuplicateEmailException.class)
	public void testCreateExistingEmail() throws Exception {
		new IDRemoteServices().createUser(ANY_USERNAME, EXISTING_EMAIL);
	}


	//--REMOVE USER --//
	
	
	@Test
	public void testRemoveUser() {
		new IDRemoteServices().removeUser(DELETING_USERNAME);
	}

	
	@Test(expected=LoginException.class)
	public void testRemoveDoesNotExist() throws Exception {		
		new IDRemoteServices().removeUser(ANY_USERNAME);
	}

	
	//--RENEW PASSWORD--//
	
	
	@Test
	public void testRenewPassword() {
		new IDRemoteServices().renewPassword(RENEWING_USERNAME);
	}
	
	@Test(expected=LoginException.class)
	public void testRenewDoesNotExist() throws Exception {
		new IDRemoteServices().renewPassword(ANY_USERNAME);
	}
	
	
	//--LOGIN USER--//
	

	@Test
	public void testLoginUser() {
		new IDRemoteServices().loginUser(EXISTING_USERNAME, EXISTING_PASSWORD);
	}
	

	@Test(expected=LoginException.class)
	public void testLoginWrongPassword() throws Exception {
		new IDRemoteServices().loginUser(EXISTING_USERNAME, WRONG_PASSWORD);
	}


}
