package pt.tecnico.sd.id;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This is a unit test in the remote perspective.
 * All tests are run in a different process from the server.
 * The server should be running while these tests are being run.
 */

public class SdIdRemoteComponentTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//TODO
		//Start the server in another process
		//There should be a pipe between the test process and the server process.
		//The server's System.in should the input side of the pipe and 
		//the server's System.out should not be altered to be displayed in the
		//test's environment console.
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
	public void testCreateUser() {
		fail("Not yet implemented"); // TODO
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
