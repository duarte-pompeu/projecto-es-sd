package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import mockit.Expectations;
import mockit.Mocked;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.junit.Test;

import pt.tecnico.bubbledocs.Cache;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Session;
import pt.tecnico.bubbledocs.domain.SuperUser;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.LoginException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

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
	
	private static final String JUBI_UNAME = "jubi";
	private static final String JUBI_PASS = "password";
	private static final String JUBI_NAME = "Jubileu Mandafacas";

	@Override
	public void populate4Test() {
		createUser(ROOT, ROOT_PASS, "Super User");
		createUser(USERNAME, PASSWORD, "Marcos Pires");
		createUser(LOGGED_IN, PASSWORD, "Manuel da Silva");
		createUser(JUBI_UNAME, JUBI_PASS, JUBI_NAME);
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
	
	
	//////////////////////////
	// TESTS FOR 2ND SPRINT //
	//////////////////////////
	
	@Mocked IDRemoteServices idRemoteMock;
	@Mocked Cache local_cache;
	
	
	/** A successful login with remote authentication.
	 */
	@Test
	public void loginWithRemote(){
		
		//FIXME: assert login is remote and not cached
		
		LoginUser service = new LoginUser(JUBI_UNAME, JUBI_PASS);
		service.dispatch();
		
		//FIXME: clean cache, not tested
		BubbleDocs bd = BubbleDocs.getInstance();
		bd.getCache().removeFromCache(JUBI_NAME);
	}
	
	/** Try to login after cache is cleaned.
	 *  The test should fail.
	 */
	@Test (expected = LoginException.class)
	public void loginAfterCleanCache(){
		
		LoginUser service = new LoginUser(JUBI_UNAME, JUBI_PASS);
		service.dispatch();
		
		Cache cache = BubbleDocs.getInstance().getCache();
		assertTrue(cache.validate(JUBI_UNAME, JUBI_PASS));
		
		cache.removeFromCache(JUBI_UNAME);
		cache.validate(JUBI_UNAME, JUBI_PASS);
	}
	
	
	/** A successful login with local authentication.
	 */
	@Test
	public void loginWithCache(){
		
		new Expectations(){{			
			idRemoteMock.loginUser(JUBI_UNAME, JUBI_PASS);
			result = new RemoteInvocationException();
		}};
		
		LoginUser service = new LoginUser(JUBI_UNAME, JUBI_PASS);
		service.dispatch();
	}
	
	
	/** An unsuccessful login because remote authentication is down
	 *  and there is no cached login.
	 *  It should fail.
	 */
	@Test(expected = UnavailableServiceException.class)
	public void noRemoteNoCache(){
		
		new Expectations(){{
			idRemoteMock.loginUser(JUBI_UNAME, JUBI_PASS);
			result = new RemoteInvocationException();
		}};
		
		new Expectations(){{
			local_cache.validate(JUBI_UNAME, JUBI_PASS);
			result = new LoginException();
		}};
		
		LoginUser service = new LoginUser(JUBI_UNAME, JUBI_PASS);
		service.dispatch();
	}
	
	
	/** A remote authentication attempt with a bad password.
	 *  It should fail.
	 */
	@Test (expected = LoginException.class)
	public void loginRemoteBadPass(){
		String bad_pass = "Ah ah ah, you didn't say the magic word.";
		
		LoginUser service = new LoginUser(JUBI_UNAME, bad_pass);
		service.dispatch();
	}
	
	
	/** A local authentication attempt with a bad password when
	 *  remote authentication is down.
	 *  It should fail.
	 */
	@Test(expected = LoginException.class)
	public void loginCacheBadPass(){
		String bad_pass = "Ah ah ah, you didn't say the magic word.";
		
		new Expectations(){{			
			idRemoteMock.loginUser(JUBI_UNAME, bad_pass);
			result = new RemoteInvocationException();
		}};
		
		LoginUser service = new LoginUser(JUBI_UNAME, bad_pass);
		service.dispatch();
	}
	
	
	/** A remote authentication attempt with a username that doesn't exist.
	 *  It should fail.
	 */
	@Test(expected = LoginException.class)
	public void loginRemoteBadUser(){
		String bad_user = "I'm root, let me in.";
		
		LoginUser service = new LoginUser(bad_user, JUBI_PASS);
		service.dispatch();
	}
	
	
	/** A local authentication attempt with a username that doesn't exist.
	 *  It should fail.
	 */
	@Test(expected = LoginException.class)
	public void loginCacheBadUser(){
		String bad_user = "I'm root, let me in.";
		
		new Expectations(){{			
			idRemoteMock.loginUser(bad_user, JUBI_PASS);
			result = new RemoteInvocationException();
		}};
		
		LoginUser service = new LoginUser(bad_user, JUBI_PASS);
		service.dispatch();
	}
}
