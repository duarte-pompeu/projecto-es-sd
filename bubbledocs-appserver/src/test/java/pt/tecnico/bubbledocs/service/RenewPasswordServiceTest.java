package pt.tecnico.bubbledocs.service;

import mockit.*;
import static org.junit.Assert.*;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.Session;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RenewPasswordServiceTest extends BubbleDocsServiceTest {

	@Mocked
	IDRemoteServices remote;
	
	private final static String USERNAME = "jack";
	private final static String EMAIL = "jack@jackson.com";
	private final static String NAME = "Jack Jackson";
	private String token;
	
	
	@Override
	public void populate4Test() {
		createUser(USERNAME, EMAIL, "silly_goose", NAME);
		token = addUserToSession(USERNAME);		
	}
	
	@Test
	public void success() {		
		RenewPassword service = new RenewPassword(token);
		service.execute();
		
		User user = getUserFromUsername(USERNAME);
		Session session = getSessionFromToken(token);

		new Verifications() {{
			remote.renewPassword(USERNAME); times = 1;
		}};
		
		assertEquals("username should not be changed", user.getUserName(), USERNAME);
		assertEquals("e-mail should not be changed", user.getEmail(), EMAIL);
		assertEquals("name should not be changed", user.getName(), NAME);
		assertNull("password must be null", user.getPassword());
		assertEquals("should be the same token", session.getToken(), token);
		assertFalse("user should still be in session", session.isExpired());
	}
	
	@Test(expected = UnavailableServiceException.class)
	public void unavailable() {
		new Expectations() {{
			remote.renewPassword(USERNAME); times = 1;
			result = new RemoteInvocationException();
		}};
		
		RenewPassword service = new RenewPassword(token);
		service.execute();
	}
	
	@Test(expected = UserNotInSessionException.class)
	public void expired() {
		removeUserFromSession(token);
		RenewPassword service = new RenewPassword(token);
		service.execute();
		
		new Verifications() {{ //verify the service was not called
			remote.renewPassword(anyString); times = 0;
		}};
	}
	
	@Test(expected = UserNotInSessionException.class)
	public void invalid() {
		RenewPassword service = new RenewPassword("I am a silly token");
		service.execute();
		
		new Verifications() {{ //verify the service was not called
			remote.renewPassword(anyString); times = 0;
		}};
	}

}
