package pt.tecnico.bubbledocs.service.remote;

import pt.tecnico.bubbledocs.exceptions.*;
import static org.junit.Assert.*;

import org.junit.Test;

import pt.tecnico.sd.ClientTicket;
import pt.tecnico.sd.id.cli.SdIdClient;
import mockit.*;
import pt.ulisboa.tecnico.sdis.id.ws.*;
import pt.tecnico.sd.id.cli.*;

public class IDRemoteServicesUnitTest {

	private static String USERNAME = "fausto";
	private static String NOT_EXISTING_USERNAME="daSilva";
	private static String EMAIL = "fausto@example.com";
	private static String EXISTING_USERNAME = "alice";
	private static String EXISTING_EMAIL = "alice@tecnico.pt";
	private static String EXISTING_PASSWORD = "Aaa1";
	private static String DELETING_USERNAME = "carla";
	private static String RENEWING_USERNAME = "bruno";
	private static String WRONG_PASSWORD = "hunter2";
	
	
	@Mocked
	SdIdClient client;
	
	
	//--CREATE USER--//
	
	
	@Test
	public void testCreateUser() throws Exception {
		new IDRemoteServices().createUser(USERNAME, EMAIL);
		
		new Verifications() {{
			client.createUser(USERNAME, EMAIL); 
			times = 1;
		}};
	}
	
	@Test(expected=InvalidUsernameException.class)
	public void testCreateInvalidUser1() throws Exception {
		new Expectations() {{
			client.createUser("", EMAIL);
			result = new InvalidUser_Exception("message", new InvalidUser());
		}};
		
		new IDRemoteServices().createUser("", EMAIL);
	}
	
	@Test(expected=InvalidUsernameException.class)
	public void testCreateInvalidUser2() throws Exception {
		new Expectations() {{
			client.createUser(null, EMAIL);
			result = new InvalidUser_Exception("message", new InvalidUser());
		}};
		
		new IDRemoteServices().createUser(null, EMAIL);
	}
	
	@Test(expected=InvalidEmailException.class)
	public void testCreateInvalidEmail1() throws Exception {
		new Expectations() {{
			client.createUser(USERNAME, "");
			result = new InvalidEmail_Exception("message", new InvalidEmail());
		}};
		
		new IDRemoteServices().createUser(USERNAME, "");
	}
	
	@Test(expected=InvalidEmailException.class)
	public void testCreateInvalidEmail2() throws Exception {
		new Expectations() {{
			client.createUser(USERNAME, null);
			result = new InvalidEmail_Exception("message", new InvalidEmail());
		}};
		
		new IDRemoteServices().createUser(USERNAME, null);
	}
	
	@Test(expected=DuplicateUsernameException.class)
	public void testCreateExistingUser() throws Exception {
		new Expectations() {{
			client.createUser(EXISTING_USERNAME, EMAIL);
			result = new UserAlreadyExists_Exception("message", new UserAlreadyExists());
		}};
		
		new IDRemoteServices().createUser(EXISTING_USERNAME, EMAIL);
	}
	
	@Test(expected=DuplicateEmailException.class)
	public void testCreateExistingEmail() throws Exception {
		new Expectations() {{
			client.createUser(USERNAME, EXISTING_EMAIL);
			result = new EmailAlreadyExists_Exception("message", new EmailAlreadyExists());
		}};
		
		new IDRemoteServices().createUser(USERNAME, EXISTING_EMAIL);
	}
	
	@Test(expected=RemoteInvocationException.class)
	public void testCreateUserFails() throws Exception {
		new Expectations() {{
			client.createUser(USERNAME, EMAIL);
			result = new SdIdRemoteException();
		}};
		
		new IDRemoteServices().createUser(USERNAME, EMAIL);
	}
	
	
	//--REMOVE USER--//

	@Test
	public void testRemoveUser() throws Exception {
		new IDRemoteServices().removeUser(DELETING_USERNAME);
		
		new Verifications() {{
			client.removeUser(DELETING_USERNAME);
			times = 1;
		}};
	}
	
	@Test(expected=LoginException.class)
	public void testRemoveDoesNotExist() throws Exception {
		new Expectations() {{
			client.removeUser(NOT_EXISTING_USERNAME);
			result = new UserDoesNotExist_Exception("message", new UserDoesNotExist());
		}};
		
		new IDRemoteServices().removeUser(NOT_EXISTING_USERNAME);
	}
	
	@Test(expected=RemoteInvocationException.class)
	public void testRemoveFails() throws Exception {
		new Expectations() {{
			client.removeUser(DELETING_USERNAME);
			result = new SdIdRemoteException();
		}};
		
		new IDRemoteServices().removeUser(DELETING_USERNAME);
	}
	
	
	//--RENEW PASSWORD--//

	@Test
	public void testRenewPassword() throws Exception {
		new IDRemoteServices().renewPassword(RENEWING_USERNAME);
		
		new Verifications() {{
			client.renewPassword(RENEWING_USERNAME);
			times = 1;
		}};
	}
	
	@Test(expected=LoginException.class)
	public void testRenewDoesNotExist() throws Exception {
		new Expectations() {{
			client.renewPassword(NOT_EXISTING_USERNAME);
			result = new UserDoesNotExist_Exception("message", new UserDoesNotExist());
		}};
		
		new IDRemoteServices().renewPassword(NOT_EXISTING_USERNAME);
	}
	
	@Test(expected=RemoteInvocationException.class)
	public void testRenewFails() throws Exception {
		new Expectations() {{
			client.renewPassword(RENEWING_USERNAME);
			result = new SdIdRemoteException();
		}};
		
		new IDRemoteServices().renewPassword(RENEWING_USERNAME);
	}
	
	
	//--LOGIN USER--//
	

	@Test
	public void testLoginUser() throws Exception {
	    String ticketBlob = "TiCkeTBlo0B";
		byte[] authenticator = { 
			0x76, 0x18, 0x20, 0x5b, 0x53, 0x18, 0x5d, 0x28, 0x34, 0x2c, 0x49, 0x16,
	        0x20, 0x54, 0x2a, 0x46, 0x57, 0x20, 0x3e, 0x72, 0x76, 0x14, 0x61, 0x54  
	    };		
		ClientTicket ticket = new ClientTicket(ticketBlob, authenticator);
		
		new Expectations() {{
			client.requestAuthentication(EXISTING_USERNAME, EXISTING_PASSWORD.getBytes());
			result = ticket;
			times = 1;
		}};
		
		ClientTicket result = new IDRemoteServices().loginUser(EXISTING_USERNAME, EXISTING_PASSWORD);
		
		assertEquals("ticket blob is different", ticketBlob, result.getTicketBlob());
		assertArrayEquals("authenticator is different", authenticator, result.getSessionKey().getEncoded());
	}
	
	@Test(expected=LoginException.class)
	public void testLoginUsernameDoesNotExist() throws Exception {
		new Expectations() {{
			client.requestAuthentication(NOT_EXISTING_USERNAME, EXISTING_PASSWORD.getBytes());
			result = new AuthenticationException();
		}};
		
		new IDRemoteServices().loginUser(NOT_EXISTING_USERNAME, EXISTING_PASSWORD);
	}
	
	@Test(expected=LoginException.class)
	public void testLoginWrongPassword() throws Exception {
		new Expectations() {{
			client.requestAuthentication(EXISTING_USERNAME, WRONG_PASSWORD.getBytes());
			result = new AuthenticationException();
		}};
		
		new IDRemoteServices().loginUser(EXISTING_USERNAME, WRONG_PASSWORD);
	}
	
	@Test(expected=RemoteInvocationException.class)
	public void testLoginFails() throws Exception {
		new Expectations() {{
			client.requestAuthentication(EXISTING_USERNAME, EXISTING_PASSWORD.getBytes());
			result = new SdIdRemoteException();
		}};
		
		new IDRemoteServices().loginUser(EXISTING_USERNAME, EXISTING_PASSWORD);
	}

}
