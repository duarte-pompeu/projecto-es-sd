package pt.tecnico.sd.id.cli;

import static org.junit.Assert.*;

import javax.xml.registry.JAXRException;
import javax.xml.ws.*;

import mockit.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pt.tecnico.sd.id.cli.SdIdClient;
import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.SDId_Service;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

/**
 * This is a unit test in the remote perspective.
 * All tests are run in a different process from the server.
 * The server should be running while these tests are being run.
 */

public class SdIdRemoteComponentTest {

	private static String[] usernames;
	private static String[] emails;
	private static String[] existingUsernames;
	private static String[] existingEmails;
	//failed in a sense where both username and email don't exist in SD-ID
	private final  String   failedUsername = "elefante";
	private final  String   failedEmail = "elefante@whoooomp.biz";
	private final  String   altUsername = "quack";
	private final  String   altEmail = "mufasa@rei-leao.cg";
	private final  String   repeatedUserName = "carla";
	private final  String   invalidUserName = "who am i";
	private final  String   repeatedEmail = "carla@tecnico.pt";
	private final  String   invalidEmail = "1600 Pennsylvania Avenue";
	private final  byte[]   originalPassword = "Bbb2".getBytes();
	private final  byte[]   password = "Eee5".getBytes();
	private final  byte[]   anyPassword = "hunter2".getBytes();
	private static SdIdClient CLIENT;



	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//TODO
		//Start the server in another process
		//There should be two pipes between the test process and the server.
		//In one pipe, the server will send its stdout to the test process
		//The test process is waiting to read this character
		//       This character ---> '%'
		//If the character isn't read, then the server didn't start properly.
		//In another pipe, the test process will send a '\n' to end the server.

		//Start server process 

		CLIENT = SdIdClient.getInstance();
		usernames = new String[]{"focas", "leao", "pato", "gato"};
		emails = new String[] {
				"dafocas@tecnico.pt", 
				"sportinguista@tecnico.pt", 
				"quack@duckland.com",
				"miau@neko.jp"
		};
		existingUsernames = new String[]{"alice", "bruno", "carla", "duarte", "eduardo"};
		existingEmails = new String[]{"alice@tecnico.pt"};
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		//TODO
		//Use the output side of the pipe to send '\n'
		//To signal the finish of the server's process
		//Wait for the end of the server process to avoid virtual zombies.
		//(Does java have zombie processes?)
	}

	/**
     *  In this test the server is mocked to
     *  simulate a communication exception.
     */
    @Test(expected=WebServiceException.class)
    public void testMockServerException(
        @Mocked final SDId_Service service,
        @Mocked final SDId port)
        throws Exception {

        // an "expectation block"
        // One or more invocations to mocked types, causing expectations to be recorded.
        new Expectations() {{
        	new SDId_Service(); 
        	service.getSDIdImplPort(); result = port;
            port.createUser(anyString, anyString);
            result = new WebServiceException("fabricated");
        }};


        // Unit under test is exercised.
        SdIdClient client = new SdIdClient();
		client.createUser("someUsername", "some.email@example.com");
    }
    
    
    /**
     *  In this test the server is mocked to
     *  simulate a communication exception on a second call.
     */
    @Test
    public void testMockServerExceptionOnSecondCall(
    	@Mocked final SDId_Service service,
        @Mocked final SDId port)
        throws Exception {

        // an "expectation block"
        // One or more invocations to mocked types, causing expectations to be recorded.
        new Expectations() {{
        	new SDId_Service(); 
        	service.getSDIdImplPort(); result = port;
            port.createUser(anyString, anyString);
            // first call to sum returns the result
            result = "ignoring this result";
            // second call throws an exception
            result = new WebServiceException("fabricated");
        }};


        // Unit under test is exercised.
        SdIdClient client = new SdIdClient();

        // first call to mocked server
        try {
        	client.createUser(usernames[3], emails[3]);
        } catch(WebServiceException e) {
            // exception is not expected
            fail();
        }

        // second call to mocked server
        try {
        	client.createUser(usernames[3], emails[3]);
            fail();
        } catch(WebServiceException e) {
            // exception is expected
            assertEquals("fabricated", e.getMessage());
        }
    }
    

	public void testCreateUser() throws Exception {
		SdIdClient client = CLIENT;
		client.createUser(usernames[0], emails[1]);
	}

	@Test(expected=UserAlreadyExists_Exception.class)
	public void testCreateUserWithSameUsername() throws Exception {
		SdIdClient client = CLIENT;
		client.createUser(usernames[1], emails[1]);

		//try to create user again 
		//if an exception is thrown it means that the user was successfully created

		client.createUser(usernames[1], altEmail);

	}

	@Test(expected=EmailAlreadyExists_Exception.class)
	public void testCreateUserWithSameEmail() throws EmailAlreadyExists_Exception, InvalidEmail_Exception, InvalidUser_Exception, UserAlreadyExists_Exception, JAXRException {
		SdIdClient client = CLIENT;
		client.createUser(usernames[2], emails[2]);

		//try to create user again 
		//if an exception is thrown it means that the user was successfully created

		client.createUser(altUsername, emails[2]);

	}

	//trying to create a user with an email that already exists
	@Test(expected=EmailAlreadyExists_Exception.class)
	public void testCreateUserEmailAlreadyExists() throws Exception {
		SdIdClient client = CLIENT;
		client.createUser(failedUsername, existingEmails[0]);
	}

	//trying to create a user with a user name that already exists
	@Test(expected=UserAlreadyExists_Exception.class)
	public void testCreateUserUsernameAlreadyExists() throws Exception {
		SdIdClient client = CLIENT;
		client.createUser(existingUsernames[0], failedEmail);
	}

	//trying to add a user with an invalid email
	@Test(expected=InvalidEmail_Exception.class)
	public void testCreateUserInvalidEmail() throws Exception {
		SdIdClient client = CLIENT;
		client.createUser(failedUsername, invalidEmail);
	}
	
	@Test(expected=InvalidEmail_Exception.class)
	public void testCreateUserEmptyEmail() throws Exception {
		SdIdClient client = CLIENT;
		client.createUser(failedUsername, "");
	}
	
	@Test(expected=InvalidEmail_Exception.class)
	public void testCreateUserNullEmail() throws Exception {
		SdIdClient client = CLIENT;
		client.createUser(failedUsername, null);
	}

	//trying to add a user with an invalid user name
	@Test(expected=InvalidUser_Exception.class)
	public void testCreateUserInvalidUserName1() throws Exception {
		SdIdClient client = CLIENT;
		client.createUser(null, failedEmail);
	}

	//trying to add a user with an invalid user name
	@Test(expected=InvalidUser_Exception.class)
	public void testCreateUserInvalidUserName2() throws Exception {
		SdIdClient client = CLIENT;
		client.createUser("", failedEmail);
	}

	
	public void testRenewPassword() throws Exception {
		SdIdClient client = CLIENT;
		client.renewPassword(existingUsernames[0]);
	}

	@Test(expected=AuthReqFailed_Exception.class)
	public void testRenewPasswordAndAuthenticateWithOldPass() throws Exception {
		SdIdClient client = CLIENT;
		client.renewPassword(existingUsernames[1]);

		//user try to authenticate with old password
		//if and exception is throw the password was successfully changed
		client.requestAuthentication(existingUsernames[1], originalPassword);
	}

	@Test(expected = UserDoesNotExist_Exception.class)
	public void testRenewPasswordDoesNotExist() throws Exception {
		SdIdClient client = CLIENT;
		client.renewPassword(invalidUserName);

	}

	public void testRemoveUser() throws Exception {
		SdIdClient client = CLIENT;
		client.removeUser(existingUsernames[2]);
	}
	
	@Test(expected=UserDoesNotExist_Exception.class)
	public void testRemoveUserTwice() throws Exception {
		SdIdClient client = CLIENT;
		client.removeUser(existingUsernames[3]);

		//try to remove user again 
		//if an exception is thrown it means that the user was sucessfully removed

		client.removeUser(existingUsernames[3]);
	}

	//trying to remove a user with an invalid user name
	@Test(expected=UserDoesNotExist_Exception.class)
	public void testRemoveUserInvalidUserName1() throws Exception {
		SdIdClient client = CLIENT;
		client.removeUser(null);
	}

	//trying to remove a user with an invalid user name
	@Test(expected=UserDoesNotExist_Exception.class)
	public void testRemoveUserInvalidUserName2() throws Exception {
		SdIdClient client = CLIENT;
		client.removeUser("");
	}

	@Test
	public void testRequestAuthentication() throws AuthReqFailed_Exception, JAXRException {
		SdIdClient client = CLIENT;
		byte[] result = client.requestAuthentication(existingUsernames[4], password);
		assertArrayEquals("wrong password", "1".getBytes(), result);

	}

	//trying to authenticate a user with an invalid user name
	@Test(expected=AuthReqFailed_Exception.class)
	public void testRequestAuthenticationUserInexistent() throws Exception {
		SdIdClient client = CLIENT;
		client.requestAuthentication(invalidUserName, anyPassword);
	}

	//trying to authenticate a user with an invalid user name
	@Test(expected=AuthReqFailed_Exception.class)
	public void testRequestAuthenticationUserInvalidUserName1() throws Exception {
		SdIdClient client = CLIENT;
		client.requestAuthentication(null, anyPassword);
	}

	//trying to authenticate a user with an invalid user name
	@Test(expected=AuthReqFailed_Exception.class)
	public void testRequestAuthenticationUserInvalidUserName2() throws Exception {
		SdIdClient client = CLIENT;
		client.requestAuthentication("", anyPassword);
	}

	//trying to authenticate a user with an invalid password
	@Test(expected=AuthReqFailed_Exception.class)
	public void testRequestAuthenticationUserInvalidPassword() throws Exception {
		SdIdClient client = CLIENT;
		client.requestAuthentication(existingUsernames[4], anyPassword);
	}

}
