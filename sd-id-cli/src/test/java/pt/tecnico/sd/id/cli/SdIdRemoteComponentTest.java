package pt.tecnico.sd.id.cli;

import static org.junit.Assert.*;
import example.ws.uddi.UDDINaming;

import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.*;

import mockit.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pt.tecnico.sd.id.SdIdClient;
import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.SDId_Service;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;
import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

/**
 * This is a unit test in the remote perspective.
 * All tests are run in a different process from the server.
 * The server should be running while these tests are being run.
 */

public class SdIdRemoteComponentTest {

	private final String userName = "focas";
	private final String ALTuserName = "quack";
	private final String email = "datfocas@tecnico.pt";
	private final String repeatedUserName = "carla";
	private final String invalidUserName = "who am i";
	private final String repeatedEmail = "carla@tecnico.pt";
	private final String invalidEmail = "invalidemail";
	private final String existingUserName = "alice";
	private final byte[] password = "Aaa1".getBytes();
	private final byte[] invalidPassword = "imBad".getBytes();
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
        SdIdClient client = CLIENT;
		client.createUser(userName, email);
    }
	
	
	
	
		@Test(expected=UserAlreadyExists_Exception.class)
		public void testCreateUser1() throws EmailAlreadyExists_Exception, InvalidEmail_Exception, InvalidUser_Exception, UserAlreadyExists_Exception, JAXRException {
		SdIdClient client = CLIENT;
		client.createUser(userName, email);
		
		//try to create user again 
		//if an exception is thrown it means that the user was successfully created
		
		client.createUser(userName, email);
			  
		}
		
		@Test(expected=EmailAlreadyExists_Exception.class)
		public void testCreateUser2() throws EmailAlreadyExists_Exception, InvalidEmail_Exception, InvalidUser_Exception, UserAlreadyExists_Exception, JAXRException {
		SdIdClient client = CLIENT;
		client.createUser(userName, email);
		
		//try to create user again 
		//if an exception is thrown it means that the user was successfully created
		
		client.createUser(ALTuserName, email);
			  
		}
	
		//trying to create a user with an email that already exists
		@Test(expected=EmailAlreadyExists_Exception.class)
		public void testCreateUserEmailAlreadyExists() throws Exception {
			SdIdClient client = CLIENT;
			client.createUser(userName, repeatedEmail);
		}
		
		//trying to create a user with a user name that already exists
		@Test(expected=UserAlreadyExists_Exception.class)
		public void testCreateUserUsernameAlreadyExists() throws Exception {
			SdIdClient client = CLIENT;
			client.createUser(repeatedUserName, email);
		}
		
		//trying to add a user with an invalid email
		@Test(expected=InvalidEmail_Exception.class)
		public void testCreateUserInvalidEmail() throws Exception {
			SdIdClient client = CLIENT;
			client.createUser(userName, invalidEmail);
		}
		
		//trying to add a user with an invalid user name
		@Test(expected=InvalidUser_Exception.class)
		public void testCreateUserInvalidUserName1() throws Exception {
			SdIdClient client = CLIENT;
			client.createUser(null, email);
		}
		
		//trying to add a user with an invalid user name
		@Test(expected=InvalidUser_Exception.class)
		public void testCreateUserInvalidUserName2() throws Exception {
			SdIdClient client = CLIENT;
			client.createUser("", email);
		}
	

		@Test(expected=AuthReqFailed_Exception.class)
		public void testRenewPassword() throws UserDoesNotExist_Exception, AuthReqFailed_Exception, JAXRException {
			SdIdClient client = CLIENT;
			client.renewPassword(existingUserName);
			
			//user try to authenticate with old password
			//if and exception is throw the password was successfully changed
			client.requestAuthentication(existingUserName, password);
		}
		
		@Test(expected = UserDoesNotExist_Exception.class)
		public void testRenewPasswordDoesNotExist() throws Exception {
			SdIdClient client = CLIENT;
			client.renewPassword(invalidUserName);
		
		}
		
		@Test(expected=UserDoesNotExist_Exception.class)
		public void testRemoveUser() throws UserDoesNotExist_Exception, JAXRException {
			SdIdClient client = CLIENT;
			client.removeUser(userName);
			
			//try to remove user again 
			//if an exception is thrown it means that the user was sucessfully removed
			
			client.removeUser(userName);
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
			byte[] result = client.requestAuthentication(existingUserName, password);
			assertArrayEquals("wrong password", "1".getBytes(),result);
	
		}
		
		//trying to authenticate a user with an invalid user name
		@Test(expected=AuthReqFailed_Exception.class)
		public void testRequestAuthenticationUserInexistent() throws Exception {
			SdIdClient client = CLIENT;
			client.requestAuthentication(invalidUserName, password);
		}
		
		//trying to authenticate a user with an invalid user name
		@Test(expected=AuthReqFailed_Exception.class)
		public void testRequestAuthenticationUserInvalidUserName1() throws Exception {
			SdIdClient client = CLIENT;
			client.requestAuthentication(null, password);
		}
			
		//trying to authenticate a user with an invalid user name
		@Test(expected=AuthReqFailed_Exception.class)
		public void testRequestAuthenticationUserInvalidUserName2() throws Exception {
			SdIdClient client = CLIENT;
			client.requestAuthentication("", password);
		}
		
		//trying to authenticate a user with an invalid password
		@Test(expected=AuthReqFailed_Exception.class)
		public void testRequestAuthenticationUserInvalidPassword() throws Exception {
			SdIdClient client = CLIENT;
			client.requestAuthentication(existingUserName, invalidPassword);
		}

}
