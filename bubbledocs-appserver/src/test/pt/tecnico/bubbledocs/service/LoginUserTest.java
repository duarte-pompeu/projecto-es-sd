package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.joda.time.LocalTime;
import org.joda.time.Seconds;

import pt.tecnico.bubbledocs.dml.BubbleDocs;
import pt.tecnico.bubbledocs.dml.Session;

// add needed import declarations
public class LoginUserTest extends BubbleDocsServiceTest {

	private String danix; // the token for user danix
	private String manel; // the token for user manel
	private String root;  // the token for user root

	private static final String USERNAME = "danix";
	private static final String NON_EXISTING = "spock";
	private static final String LOGGED_IN = "manel";
	private static final String PASSWORD = "hunter2";
	private static final String DIFF_PASS = "toaster-repair";
	private static final String ANY_PASS = "I-C-I-D";

	@Override
	public void populate4Test() {
		createUser(USERNAME, PASSWORD, "Marcos Pires");
		createUser(LOGGED_IN, PASSWORD, "Manuel da Silva");
		manel = addUserToSession(LOGGED_IN);
	}

	// returns the time of the last access for the user with token userToken.
	// It must get this data from the session object of the application
	private LocalTime getLastAccessTimeInSession(String userToken) {
		Session s = getSessionFromToken(userToken);
		return s.getAccess();
	}

	@Test
	public void successNewToken() {
		LoginUser service = new LoginUser(USERNAME, PASSWORD);
		service.execute(); //it shouldn't explode here.
		
		LocalTime currentTime = new LocalTime();
		String token = service.getUserToken();
		User user = getUserFromSession(token);
		assertEquals(USERNAME, user.getUsername());

		int difference = Seconds.secondsBetween(getLastAccessTimeInSession(token), currentTime).getSeconds();

		assertTrue("Access time in session is correctly set", difference >= 0);
		assertTrue("diference in seconds greater than expected", difference < 2);
	}

	@Test
	public void successLoginTwiceSameToken() {
		LoginUser service = new LoginUser(LOGGED_IN, PASSWORD);

		service.execute();
		String token1 = service.getUserToken();
		LocalTime access1 = getLastAccessTimeInSession(token1);
		
		Thread.sleep(1453); //Κωνσταντινούπολη έπεσε!

		service.execute();
		String token2 = service.getUserToken();
		LocalTime access2 = getLastAccessTimeInSession(token2);
		
		int difference = Seconds.secondsBetween(access1, access2).getSeconds()
		
		assertEquals("Tokens don't change", token1, token2);
		assertTrue("Tokens were touched", difference > 0);
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
