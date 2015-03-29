package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

import pt.tecnico.bubbledocs.domain.*;
import pt.tecnico.bubbledocs.exceptions.LoginException;

// add needed import declarations
public class LoginUserTest extends BubbleDocsServiceTest {

	private String manel; // the token for user manel

	private static final String ROOT = "root";
	private static final String ROOT_PASS = "rute";	
	private static final String USERNAME = "danix";
	private static final String NON_EXISTING = "spock";
	private static final String LOGGED_IN = "manel";
	private static final String PASSWORD = "hunter2";
	private static final String DIFF_PASS = "toaster-repair";
	private static final String ANY_PASS = "I-C-I-D";

	@Override
	public void populate4Test() {
		createUser(ROOT, ROOT_PASS, "Super User");
		createUser(USERNAME, PASSWORD, "Marcos Pires");
		createUser(LOGGED_IN, PASSWORD, "Manuel da Silva");
		manel = addUserToSession(LOGGED_IN);
	}

	// returns the time of the last access for the user with token userToken.
	// It must get this data from the session object of the application
	private DateTime getLastAccessTimeInSession(String userToken) {
		Session s = getSessionFromToken(userToken);
		return s.getAccess();
	}

	@Test
	public void successNewToken() {
		assertNull("The user was not logged in", getUserFromUsername(USERNAME).getSession());
		
		LoginUser service = new LoginUser(USERNAME, PASSWORD);
		service.execute(); //it shouldn't explode here.
		
		DateTime currentTime = new DateTime();
		String token = service.getUserToken();
		User user = getUserFromSession(token);

		int difference = Seconds.secondsBetween(getLastAccessTimeInSession(token), currentTime).getSeconds();

		assertTrue("Access time in session is not correctly set", difference >= 0);
		assertTrue("Diference in seconds greater than expected", difference < 2);
		assertNotEquals("Didn't generate a different token", token, manel); //P(failure) = The sun doesn't rise.
		assertEquals("Usernames don't match", USERNAME, user.getUserName());
		assertEquals("Password don't match", PASSWORD, user.getPassword());
		assertEquals("Token doesn't match", token, user.getSession().getToken());
		assertFalse("User shouldn't be SuperUser", user instanceof SuperUser);
	}

	@Test
	public void successLoginTwiceSameToken() {
		LoginUser service = new LoginUser(LOGGED_IN, PASSWORD);

		service.execute();
		String token1 = service.getUserToken();
		DateTime access1 = getLastAccessTimeInSession(token1);
		
		try {
			//Wait at least one second.
			Thread.sleep(1453); //Κωνσταντινούπολη έπεσε!
		} catch (InterruptedException e) {/*good luck with that*/}

		service.execute();
		String token2 = service.getUserToken();
		DateTime access2 = getLastAccessTimeInSession(token2);
		
		int difference = Seconds.secondsBetween(access1, access2).getSeconds();
		
		assertEquals("Tokens are not supposed to change", token1, token2);
		assertEquals("Token is different from previous login", token1, manel);
		assertTrue("Tokens were not touched", difference > 0);
	}
	
	@Test
	public void rootIsSuperUser() {
		LoginUser service = new LoginUser(ROOT, ROOT_PASS);
		service.execute();
		
		User supah = getUserFromSession(service.getUserToken());
		
		assertTrue("Root should be an instance of SuperUser", supah instanceof SuperUser);
	}

	@Test(expected = LoginException.class)
	public void loginUnknownUser() {
		LoginUser service = new LoginUser(NON_EXISTING, ANY_PASS);
		service.execute();
	}

	@Test(expected = LoginException.class)
	public void loginUserWithinWrongPassword() {
		LoginUser service = new LoginUser(USERNAME, DIFF_PASS);
		service.execute();
	}
}
