package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import mockit.Expectations;
import mockit.Mocked;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.junit.Test;

import pt.tecnico.bubbledocs.domain.Session;
import pt.tecnico.bubbledocs.domain.SuperUser;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.LoginException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.BubbleDocsServiceTest;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.integration.LoginUserIntegrator;

// add needed import declarations
public class LoginUserIntegratorTest extends BubbleDocsServiceTest {

	private String manel; // the token for user manel

	private static final String ROOT = "root";
	private static final String ROOT_PASS = "rute";
	private static final String USERNAME = "danix";
	private static final String NON_EXISTING = "spock";
	private static final String LOGGED_IN = "manel";
	private static final String PASSWORD = "hunter2";
	private static final String DIFF_PASS = "toaster-repair";
	private static final String ANY_PASS = "I-C-I-D";
	
	private static final String JUBI_UNAME = "jubi";
	private static final String JUBI_PASS = "password";
	private static final String JUBI_NAME = "Jubileu Mandafacas";
	private static final String NO_CACHE = "Nocache";

	@Mocked IDRemoteServices remote;
	
	@Override
	public void populate4Test() {
		createUser(USERNAME,  "user@example.com",PASSWORD, "Marcos Pires");
		createUser(LOGGED_IN, "logged_in@example.com", PASSWORD, "Manuel da Silva");
		createUser(JUBI_UNAME, "jubi@example.com", JUBI_PASS, JUBI_NAME);
		createUser(NO_CACHE, "no_cache@example.com", null, "No money");
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
		new Expectations() {{
			remote.loginUser(USERNAME, PASSWORD); times = 1;
		}};
		
		assertNull("The user was not logged in", getUserFromUsername(USERNAME).getSession());
		
		LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, PASSWORD);
		service.execute(); //it shouldn't explode here.
		
		DateTime currentTime = new DateTime();
		String token = service.getResult();
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
		new Expectations() {{
			remote.loginUser(LOGGED_IN, PASSWORD); times = 2;
		}};
		
		LoginUserIntegrator service = new LoginUserIntegrator(LOGGED_IN, PASSWORD);

		service.execute();
		String token1 = service.getResult();
		DateTime access1 = getLastAccessTimeInSession(token1);
		
		try {
			//Wait at least one second.
			Thread.sleep(1453); //Κωνσταντινούπολη έπεσε!
		} catch (InterruptedException e) {/*good luck with that*/}

		service.execute();
		String token2 = service.getResult();
		DateTime access2 = getLastAccessTimeInSession(token2);
		
		int difference = Seconds.secondsBetween(access1, access2).getSeconds();
		
		assertEquals("Tokens are not supposed to change", token1, token2);
		assertEquals("Token is different from previous login", token1, manel);
		assertTrue("Tokens were not touched", difference > 0);
	}
	
	@Test
	public void rootIsSuperUser() {
		new Expectations() {{
			remote.loginUser(ROOT, ROOT_PASS); times = 1;
		}};
		
		LoginUserIntegrator service = new LoginUserIntegrator(ROOT, ROOT_PASS);
		service.execute();
		
		User supah = getUserFromSession(service.getResult());
		
		assertTrue("Root should be an instance of SuperUser", supah instanceof SuperUser);
	}

	@Test(expected = LoginException.class)
	public void loginUnknownUser() {
		new Expectations() {{
			remote.loginUser(NON_EXISTING, ANY_PASS);
			result = new LoginException();
		}};
		
		LoginUserIntegrator service = new LoginUserIntegrator(NON_EXISTING, ANY_PASS);
		service.execute();
	}

	@Test(expected = LoginException.class)
	public void loginUserWithinWrongPassword() {
		new Expectations(){{			
			remote.loginUser(USERNAME, DIFF_PASS);
			times = 1;
			result = new LoginException();
		}};
		
		LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, DIFF_PASS);
		service.execute();
	}
	
	
	//////////////////////////
	// TESTS FOR 2ND SPRINT //
	//////////////////////////
	
	
	/** A successful login with remote authentication.
	 */
	@Test
	public void loginRemote(){
		
		//FIXME: assert login is remote and not cached
		
		LoginUserIntegrator service = new LoginUserIntegrator(JUBI_UNAME, JUBI_PASS);
		service.execute();
		
		User u = this.getUserFromUsername(JUBI_UNAME);
		String utoken = u.getSession().getToken();
		String stoken = service.getResult();
		
		assertNotNull(utoken);
		assertNotNull(stoken);
		assertEquals(stoken, utoken);
	}
	
	
	/** A successful login with local authentication.
	 */
	@Test
	public void loginLocal(){
		
		new Expectations(){{			
			remote.loginUser(JUBI_UNAME, JUBI_PASS);
			result = new RemoteInvocationException();
		}};
		
		LoginUserIntegrator service = new LoginUserIntegrator(JUBI_UNAME, JUBI_PASS);
		service.execute();
		
		User u = this.getUserFromUsername(JUBI_UNAME);
		String utoken = u.getSession().getToken();
		String stoken = service.getResult();
		
		assertNotNull(utoken);
		assertNotNull(stoken);
		assertEquals(stoken, utoken);
	}
	
	
	/** An unsuccessful login because remote authentication is down
	 *  and there is no cached login.
	 *  It should fail.
	 */
	@Test(expected = UnavailableServiceException.class)
	public void noRemoteNoLocal(){
		
		new Expectations(){{
			remote.loginUser(NO_CACHE, "hunter2");
			result = new RemoteInvocationException();
		}};
		
		LoginUserIntegrator service = new LoginUserIntegrator(NO_CACHE, "hunter2");
		service.execute();
	}
	
	
	/** Try to login after "cache is cleaned".
	 *  Since we got rid of the cache concept, we just set user's stored password to null.
	 *  The test should fail.
	 */
	@Test (expected = UnavailableServiceException.class)
	public void loginAfterCleanLocal(){
		String temp_username = "abcd123";
		String temp_password = "abd123";
		String temp_name = "abcd123";
		String temp_mail = "abc@def.ghi";
		
		// simulate an exception with a mock
		new Expectations(){{			
			remote.loginUser(temp_username, temp_password);
			result = new RemoteInvocationException();
		}};
		
		createUser(temp_username, temp_mail, temp_password, temp_name);
		LoginUserIntegrator service = new LoginUserIntegrator(temp_username, temp_password);
		service.execute();
		
		User u = this.getUserFromUsername(temp_username);
		u.setPassword(null);
		
		service.execute();
	}
	
	
	/** A remote authentication attempt with a bad password.
	 *  It should fail.
	 */
	@Test (expected = LoginException.class)
	public void loginRemoteBadPass(){
		String bad_pass = "Ah ah ah, you didn't say the magic word.";
		
		new Expectations(){{			
			remote.loginUser(JUBI_UNAME, bad_pass);
			result = new LoginException();
		}};
		
		LoginUserIntegrator service = new LoginUserIntegrator(JUBI_UNAME, bad_pass);
		service.execute();
	}
	
	
	/** A local authentication attempt with a bad password when
	 *  remote authentication is down.
	 *  It should fail.
	 */
	@Test(expected = UnavailableServiceException.class)
	public void loginLocalBadPass(){
		String bad_pass = "Ah ah ah, you didn't say the magic word.";
		
		new Expectations(){{			
			remote.loginUser(JUBI_UNAME, bad_pass);
			result = new RemoteInvocationException();
		}};
		
		LoginUserIntegrator service = new LoginUserIntegrator(JUBI_UNAME, bad_pass);
		service.execute();
	}
	
	
	/** A remote authentication attempt with a username that doesn't exist.
	 *  It should fail.
	 */
	@Test(expected = LoginException.class)
	public void loginRemoteBadUser(){
		String bad_user = "I'm root, let me in.";
		
		new Expectations() {{
			remote.loginUser(bad_user, JUBI_PASS);
			result = new LoginException();
		}};
		
		LoginUserIntegrator service = new LoginUserIntegrator(bad_user, JUBI_PASS);
		service.execute();
	}
}
