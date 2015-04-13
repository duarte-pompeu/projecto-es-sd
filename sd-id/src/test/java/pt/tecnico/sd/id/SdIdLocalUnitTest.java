package pt.tecnico.sd.id;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.registry.JAXRException;

import mockit.Mock;
import mockit.MockUp;
import pt.ulisboa.tecnico.sdis.id.ws.*;
import example.ws.uddi.*;
/**
 * This is a unit test in the local perspective.
 * All tests are run in the same process as the server class.
 * 
 * BeforeClass, AfterClass, Before and After methods are declared
 * because they could be useful in designing this test.
 * They can be left empty or removed.
 */
 
 
public class SdIdLocalUnitTest {

	private SDIdImpl sdIdService;
	
	private final String userName = "alice";
	private final String invalidUserName = "who am i";
	private final String email = "alice@tecnico.pt";
	private final byte[] password = "Aaa1".getBytes();
	private final byte[] invalidPassword = "imBad".getBytes();
	
	private final String userName1 = "user1";
	private final String userName2 = "user2";
	private final String repeatedUserName = "carla";
	private final String email1 = "a@t";
	private final String email2 = "b@t";
	private final String repeatedEmail = "carla@tecnico.pt";
	private final String invalidEmail = "invalidemail";

	//Mock class simulating a failing behaviour at the uddi naming service
	public static class MockUDDINamingFails extends MockUp<UDDINaming>
	{
		   @Mock
		   public void $init() {}

		   @Mock
		   public void rebind(String name, String url) throws JAXRException
		   {
		      throw new JAXRException();
		   }
		}
		
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//Maybe empty
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		//Maybe empty
	}

	@Before
	public void setUp() throws Exception {
		this.sdIdService = new SDIdImpl(new UserTable());
	}

	@After
	public void tearDown() throws Exception {
		//Maybe empty
	}

	//the standard scenario
	@Test
	public void testCreateUser() throws Exception {
		User user1=null, user2=null;
		sdIdService.createUser(userName1, email1);
		sdIdService.createUser(userName2, email2);
		
		if((user1=sdIdService.getUserByUsername(userName1))==null || 
			(user2=sdIdService.getUserByUsername(userName2))==null )
			fail();
		if(!user1.getEmail().equals(email1) || !user2.getEmail().equals(email2))
			fail();
	}
	//trying to add a user with an email that already exists
	@Test(expected=EmailAlreadyExists_Exception.class)
	public void testCreateUserEmailAlreadyExists() throws Exception {
		sdIdService.createUser(userName1, repeatedEmail);
	}
	//trying to add a user with a user name that already exists
	@Test(expected=UserAlreadyExists_Exception.class)
	public void testCreateUserUsernameAlreadyExists() throws Exception {
		sdIdService.createUser(repeatedUserName, email1);
	}
	//trying to add a user with an invalid email
	@Test(expected=InvalidEmail_Exception.class)
	public void testCreateUserInvalidEmail() throws Exception {
		sdIdService.createUser(userName1, invalidEmail);
	}
	//trying to add a user with an invalid user name
	@Test(expected=InvalidUser_Exception.class)
	public void testCreateUserInvalidUserName1() throws Exception {
		sdIdService.createUser(null, email1);
	}
	
	//trying to add a user with an invalid user name
	@Test(expected=InvalidUser_Exception.class)
	public void testCreateUserInvalidUserName2() throws Exception {
		sdIdService.createUser("", email1);
	}
	
	@Test
	public void testRenewPassword() throws Exception {
		sdIdService.renewPassword(userName);
				
		User alice = sdIdService.getUserByUsername(userName);
		//Password must be different because generated passwords have 8 characters.
		assertFalse("password must be different", Arrays.equals(alice.password, password));
		assertEquals("email cannot be changed", alice.email, email);
		assertEquals("strange, username cannot change", alice.username, userName);
	}
	
	@Test(expected = UserDoesNotExist_Exception.class)
	public void testRenewPasswordDoesNotExist() throws Exception {
		sdIdService.renewPassword("spock");
	}

	@Test
	public void testRemoveUser() throws UserDoesNotExist_Exception, EmailAlreadyExists_Exception, InvalidEmail_Exception, InvalidUser_Exception, UserAlreadyExists_Exception {
		sdIdService.createUser(userName1, email1);
		sdIdService.createUser(userName2, email2);
		 
		//time 2 remove
		sdIdService.removeUser(userName1);
		sdIdService.removeUser(userName2);
		
		if ((sdIdService.getUserByUsername(userName1))!=null)
				fail();
			
		if ((sdIdService.getUserByUsername(userName2))!=null)
			fail();
	}
	
	//trying to remove a user with an invalid user name
	@Test(expected=UserDoesNotExist_Exception.class)
	public void testRemoveUserInexistent() throws Exception {
		sdIdService.removeUser(invalidUserName);
	}
	
	//trying to remove a user with an invalid user name
	@Test(expected=UserDoesNotExist_Exception.class)
	public void testRemoveUserInvalidUserName1() throws Exception {
		sdIdService.removeUser(null);
	}
		
	//trying to remove a user with an invalid user name
	@Test(expected=UserDoesNotExist_Exception.class)
	public void testRemoveUserInvalidUserName2() throws Exception {
		sdIdService.removeUser("");
	}
	
	@Test
	public void testRequestAuthentication() throws AuthReqFailed_Exception {
		User alice = sdIdService.getUserByUsername(userName);
		sdIdService.requestAuthentication(userName, password);
		if ((sdIdService.getUserByUsername(userName))==null)
			fail();
		if(!Arrays.equals(alice.password, password))
			fail();
	}
	
	//trying to authenticate a user with an invalid user name
	@Test(expected=AuthReqFailed_Exception.class)
	public void testRequestAuthenticationUserInexistent() throws Exception {
		sdIdService.requestAuthentication(invalidUserName, password);
	}
	
	//trying to authenticate a user with an invalid user name
	@Test(expected=AuthReqFailed_Exception.class)
	public void testRequestAuthenticationUserInvalidUserName1() throws Exception {
		sdIdService.requestAuthentication(null, password);
	}
		
	//trying to authenticate a user with an invalid user name
	@Test(expected=AuthReqFailed_Exception.class)
	public void testRequestAuthenticationUserInvalidUserName2() throws Exception {
		sdIdService.requestAuthentication("", password);
	}
	
	//trying to authenticate a user with an invalid password
	@Test(expected=AuthReqFailed_Exception.class)
	public void testRequestAuthenticationUserInvalidPassword() throws Exception {
		sdIdService.requestAuthentication(userName, invalidPassword);
	}

}
