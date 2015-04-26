package pt.tecnico.bubbledocs.service.remote;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.tecnico.sd.id.cli.SdIdClient;
import mockit.*;


public class IDRemoteServicesUnitTest {

	private static String USERNAME = "fausto";
	private static String EMAIL = "fausto@example.com";
	private static String EXISTING_USERNAME = "alice";
	private static String EXISTING_PASSWORD = "Aaa1";
	private static String DELETING_USERNAME = "carla";
	private static String RENEWING_USERNAME = "bruno";
	
	
	@Mocked
	SdIdClient client;
	
	@Test
	public void testCreateUser() throws Exception {
		new IDRemoteServices().createUser(USERNAME, EMAIL);
		
		new Verifications() {{
			client.createUser(USERNAME, EMAIL); 
			times = 1;
		}};
	}

	@Test
	public void testLoginUser() throws Exception {
		new Expectations() {{
			client.requestAuthentication(EXISTING_USERNAME, EXISTING_PASSWORD.getBytes());
			result = "1".getBytes();
			times = 1;
		}};
		
		new IDRemoteServices().loginUser(EXISTING_USERNAME, EXISTING_PASSWORD);			
	}

	@Test
	public void testRemoveUser() throws Exception {
		new IDRemoteServices().removeUser(DELETING_USERNAME);
		
		new Verifications() {{
			client.removeUser(DELETING_USERNAME);
			times = 1;
		}};
	}

	@Test
	public void testRenewPassword() throws Exception {
		new IDRemoteServices().renewPassword(RENEWING_USERNAME);
		
		new Verifications() {{
			client.renewPassword(RENEWING_USERNAME);
			times = 1;
		}};
	}

}
