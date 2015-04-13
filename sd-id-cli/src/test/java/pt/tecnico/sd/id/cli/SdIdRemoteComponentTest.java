package pt.tecnico.sd.id.cli;

import static org.junit.Assert.*;
import example.ws.uddi.UDDINaming;
import java.util.Map;
import javax.xml.ws.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

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
	    System.out.printf("Contacting UDDI at %s%n", uddiURL);
	    UDDINaming uddiNaming = new UDDINaming(uddiURL);

	    System.out.printf("Looking for '%s'%n", name);
	    String endpointAddress = uddiNaming.lookup(name);

	    if (endpointAddress == null) {
		System.out.println("Not found!");
		throw new RuntimeException("endpoint address not found");
	    } else {
		System.out.printf("Found %s%n", endpointAddress);
	    }
	
	    System.out.println("Creating stub ...");
	    HelloImplService service = new HelloImplService();
	    port = service.getHelloImplPort();
	
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
