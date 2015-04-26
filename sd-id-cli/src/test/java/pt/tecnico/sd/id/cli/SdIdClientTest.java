package pt.tecnico.sd.id.cli;

import static org.junit.Assert.*;

import java.util.Arrays;

import javax.xml.registry.JAXRException;
import javax.xml.ws.WebServiceException;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import mockit.*;

public class SdIdClientTest {
	
	@Mocked
	SDId sdid;
	
	@Mocked
	SdIdConnector connector;
	
	private SdIdClient createSdIdClient() throws Exception {
		return new SdIdClient();
	}
	

	@Test(expected=SdIdRemoteException.class)
	public void testUddiFails() throws Exception {
		new Expectations() {{
			connector.connect(anyString, anyString); result = new JAXRException();
		}};
		
		new SdIdClient();
	}

	@Test(expected=SdIdRemoteException.class)
	public void testCreatePortFails() throws Exception {
		new Expectations() {{
			connector.connect(anyString, anyString); result = new WebServiceException();			
		}};
		
		new SdIdClient();
	}

	@Test
	public void testCreateUser() throws Exception {
		createSdIdClient().createUser("billy", "billy@example.com");
		
		new Verifications() {{
			sdid.createUser("billy", "billy@example.com"); times = 1;
		}};
	}
	
	@Test(expected=SdIdRemoteException.class)
	public void testCreateUserFails() throws Exception {
		new Expectations() {{
			sdid.createUser("billy", "billy@example.com"); result = new WebServiceException();	
		}};		
		
		createSdIdClient().createUser("billy", "billy@example.com");		
	}

	@Test
	public void testRenewPassword() throws Exception {
		createSdIdClient().renewPassword("billy");
		
		new Verifications() {{
			sdid.renewPassword("billy"); times = 1;
		}};
	}
	
	@Test(expected=SdIdRemoteException.class)
	public void testRenewPasswordFails() throws Exception {
		new Expectations() {{
			sdid.renewPassword("billy"); result = new WebServiceException();	
		}};		
		
		createSdIdClient().renewPassword("billy");		
	}

	@Test
	public void testRemoveUser() throws Exception {
		createSdIdClient().removeUser("billy");

		new Verifications() {{
			sdid.removeUser("billy"); times = 1;
		}};
	}
	
	@Test(expected=SdIdRemoteException.class)
	public void testRemoveUserFails() throws Exception {
		new Expectations() {{
			sdid.removeUser("billy"); result = new WebServiceException();	
		}};		
		
		createSdIdClient().removeUser("billy");		
	}

	@Test
	public void testRequestAuthentication() throws Exception {
		new Expectations() {{
			sdid.requestAuthentication("billy", "hunter2".getBytes()); 
			result = "1".getBytes();
		}};
		
		byte result[] = createSdIdClient().requestAuthentication("billy", "hunter2".getBytes());
		
		assertTrue(Arrays.equals(result, "1".getBytes()));
	}
	
	@Test(expected=SdIdRemoteException.class)
	public void testRequestAuthenticationFails() throws Exception {
		new Expectations() {{
			sdid.requestAuthentication("billy", "hunter2".getBytes()); result = new WebServiceException();	
		}};		
		
		createSdIdClient().requestAuthentication("billy", "hunter2".getBytes());		
	}

}
