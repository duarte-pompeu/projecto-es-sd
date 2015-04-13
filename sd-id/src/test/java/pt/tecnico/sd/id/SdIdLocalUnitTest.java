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

	private final UserTable users;
	private final SDIdImpl sdIdService;
	private final String userName1 = "alice";
	private final String userName2 = "bruno";
	private final String userName3 = "carla";
	private final String userName4 = "duarte";
	private final String userName5 = "eduardo";
	private final byte[] password1 = "Aaa1".getBytes();
	private final byte[] password2 = "Bbb2".getBytes();
	private final byte[] password3 = "Ccc3".getBytes();
	private final byte[] password4 = "Ddd4".getBytes();
	private final byte[] password5 = "Eee5".getBytes();
	private final String email = "alice@tecnico.pt";
	private final String email = "bruno@tecnico.pt";
	private final String email = "carla@tecnico.pt";
	private final String email = "duarte@tecnico.pt";
	private final String email = "eduardo@tecnico.pt";
	

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
		sdIdService=new SDIdImpl(users);
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
		sdIdService.createUser(userName3, email3);
		sdIdService.createUser(userName4, email4);
		sdIdService.createUser(userName5, email5);
	}
	
	@Test(expected=EmailAlreadyExists.class)
	public void testCreateUser() {
		sdIdService.createUser(userName1, email);
		sdIdService.createUser(userName, email);
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
