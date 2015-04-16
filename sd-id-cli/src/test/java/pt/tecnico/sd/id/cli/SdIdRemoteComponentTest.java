package pt.tecnico.sd.id.cli;

import static org.junit.Assert.*;
import example.ws.uddi.UDDINaming;

import java.util.Map;

import javax.xml.ws.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pt.tecnico.sd.id.SdIdClient;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.SDId_Service;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

/**
 * This is a unit test in the remote perspective.
 * All tests are run in a different process from the server.
 * The server should be running while these tests are being run.
 */

public class SdIdRemoteComponentTest {

	private final String userName = "focas";
	private final String email = "datfocas@tecnico.pt";
	private final String repeatedUserName = "carla";
	private final String repeatedEmail = "carla@tecnico.pt";
	private final String invalidEmail = "invalidemail";

	
	
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
		
		//Read output from server and send to this stdout
		//Keep reading until you find '%'
		//If it throws EOF then cancel the test.
		
		String uddiUrl = System.getProperty("uddi.url");
		String wsName = System.getProperty("ws.name");
		
	    System.out.printf("Contacting UDDI at %s%n", uddiUrl);
	    UDDINaming uddiNaming = new UDDINaming(uddiUrl);

	    System.out.printf("Looking for '%s'%n", wsName);
	    String endpointAddress = uddiNaming.lookup(wsName);

	    if (endpointAddress == null) {
		System.out.println("Not found!");
		throw new RuntimeException("endpoint address not found");
	    } else {
		System.out.printf("Found %s%n", endpointAddress);
	    }
	
	    System.out.println("Creating stub ...");
	    SDId_Service service = new SDId_Service(); 
	    SDId port = service.getSDIdImplPort();
	
	    System.out.println("Setting endpoint address ...");
	    BindingProvider bindingProvider = (BindingProvider) port;
	    Map<String, Object> requestContext = bindingProvider.getRequestContext();
	    requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);	    
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		//TODO
		//Use the output side of the pipe to send '\n'
		//To signal the finish of the server's process
		//Wait for the end of the server process to avoid virtual zombies.
		//(Does java have zombie processes?)
	}

	
		@Test
		public void testCreateUser() throws EmailAlreadyExists_Exception, InvalidEmail_Exception, InvalidUser_Exception, UserAlreadyExists_Exception {
		SdIdClient client = new SdIdClient();
		client.createUser(userName, email);
		
		//need feedback on this one pls
		   try {
			   client.createUser(userName, email);
			   fail();
		   } catch (UserAlreadyExists_Exception | EmailAlreadyExists_Exception e){
			   //how to pass test?
		   }
		
		}
	
		//trying to create a user with an email that already exists
		@Test(expected=EmailAlreadyExists_Exception.class)
		public void testCreateUserEmailAlreadyExists() throws Exception {
			SdIdClient client = new SdIdClient();
			client.createUser(userName, repeatedEmail);
		}
		
		//trying to create a user with a user name that already exists
		@Test(expected=UserAlreadyExists_Exception.class)
		public void testCreateUserUsernameAlreadyExists() throws Exception {
			SdIdClient client = new SdIdClient();
			client.createUser(repeatedUserName, email);
		}
		
		//trying to add a user with an invalid email
		@Test(expected=InvalidEmail_Exception.class)
		public void testCreateUserInvalidEmail() throws Exception {
			SdIdClient client = new SdIdClient();
			client.createUser(userName, invalidEmail);
		}
		
		//trying to add a user with an invalid user name
		@Test(expected=InvalidUser_Exception.class)
		public void testCreateUserInvalidUserName1() throws Exception {
			SdIdClient client = new SdIdClient();
			client.createUser(null, email);
		}
		
		//trying to add a user with an invalid user name
		@Test(expected=InvalidUser_Exception.class)
		public void testCreateUserInvalidUserName2() throws Exception {
			SdIdClient client = new SdIdClient();
			client.createUser("", email);
		}
	

	@Test
	public void testRenewPassword() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testRemoveUser() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testRequestAuthentication() {
		fail("Not yet implemented"); // TODO
	}

}
