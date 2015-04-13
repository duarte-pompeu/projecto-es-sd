package pt.tecnico.sd.id;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This is a unit test in the local perspective.
 * All tests are run in the same process as the server class.
 * 
 * BeforeClass, AfterClass, Before and After methods are declared
 * because they could be useful in designing this test.
 * They can be left empty or removed.
 */
 
 
public class SdIdLocalUnitTest {

	private final SDIdImpl sdIdService;
	private final String userName1 = "user1";
	private final String userName2 = "user2";
	private final String repeatedUserName = "carla";
	private final String email1 = "a@t";
	private final String email2 = "b@t";
	private final String repeatedEmail = "carla@tecnico.pt";
	
	

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
		users=new UserTable();
	}

	@After
	public void tearDown() throws Exception {
		//Maybe empty
	}

	//the standart scenario
	@Test
	public void testCreateUser() {
		sdIdService.createUser(userName1, email1);
		sdIdService.createUser(userName2, email2);
	}
	
	@Test(expected=EmailAlreadyExists_Exception.class)
	public void testCreateUser() {
		sdIdService.createUser(userName1, repeatedEmail);
	}
	
	@Test(expected=UserNameAlreadyExists.class)
	public void testCreateUser() {
		sdIdService.createUser(userName1, repeatedEmail);
	}

	@Test
	public void testRenewPassword() {
		fail("Not yet implemented"); //TODO
	}

	@Test
	public void testRemoveUser() {
		fail("Not yet implemented"); //TODO
	}

	@Test
	public void testRequestAuthentication() {
		fail("Not yet implemented"); //TODO
	}

}
