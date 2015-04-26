package pt.tecnico.bubbledocs.service.remote;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class IDRemoteServicesRemoteTest {
	
	private static String USERNAME = "fausto";
	private static String EMAIL = "fausto@example.com";
	private static String EXISTING_USERNAME = "alice";
	private static String EXISTING_PASSWORD = "Aaa1";
	private static String DELETING_USERNAME = "carla";
	private static String RENEWING_USERNAME = "bruno";
	
	@Test
	public void testCreateUser() {
		new IDRemoteServices().createUser(USERNAME, EMAIL);
	}

	@Test
	public void testLoginUser() {
		new IDRemoteServices().loginUser(EXISTING_USERNAME, EXISTING_PASSWORD);
	}

	@Test
	public void testRemoveUser() {
		new IDRemoteServices().removeUser(DELETING_USERNAME);
	}

	@Test
	public void testRenewPassword() {
		new IDRemoteServices().renewPassword(RENEWING_USERNAME);
	}

}
