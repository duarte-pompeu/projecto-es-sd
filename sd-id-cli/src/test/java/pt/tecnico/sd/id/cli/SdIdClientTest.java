package pt.tecnico.sd.id.cli;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

import org.junit.Test;

import pt.tecnico.sd.ClientTicket;
import pt.tecnico.sd.SdCrypto;
import pt.tecnico.sd.id.cli.handler.TicketHandler;
import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed;
import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import mockit.*;

public class SdIdClientTest {
	
	@Mocked
	SDId sdid;
	
	@Mocked
	SdIdConnector connector;
	
	private static byte[] example = {  0x12, 0x34, 0x56, 0x78, 
	           0x76, 0x18, 0x20, 0x5b, 0x53, 0x18, 0x5d, 0x28, 0x34, 0x2c, 0x49, 0x16,
	           0x20, 0x54, 0x2a, 0x46, 0x57, 0x20, 0x3e, 0x72, 0x76, 0x14, 0x61, 0x54  };
	
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
		SecretKey clientKey = SdCrypto.generateKey(SdCrypto.digestPassword("hunter2".getBytes()));
		byte[] encrypted = SdCrypto.encrypt(clientKey, example);
		String ticket = "TiCkEtBlooB";
				
		Map<String, Object> mockedContext = new HashMap<String, Object>();
		mockedContext.put(TicketHandler.TICKET_PROPERTY, ticket);
		
		new Expectations() {{
			sdid.requestAuthentication("billy", "SD-Store:123456789".getBytes()); 
			result = encrypted;
			
			connector.getResponseContext();
			result = mockedContext;
		}};
		
		ClientTicket result = createSdIdClient().requestAuthentication("billy", "hunter2".getBytes());
		
		assertArrayEquals("authenticator is different", 
				          Arrays.copyOfRange(example, 4, example.length), 
				          result.getSessionKey().getEncoded());
		assertEquals("ticket is different", ticket, result.getTicketBlob());
		
	}
	
	@Test(expected=AuthenticationException.class)
	public void testRequestAuthenticationWrongPassword() throws Exception {
		SecretKey rightKey = SdCrypto.generateKey(SdCrypto.digestPassword("correct-password".getBytes()));
		byte[] encrypted = SdCrypto.encrypt(rightKey, example);
		
		new Expectations() {{
			sdid.requestAuthentication("billy", "SD-Store:123456789".getBytes()); 
			result = encrypted;	
		}};		
		
		createSdIdClient().requestAuthentication("billy", "hunter2".getBytes());		
	}
	
	@Test(expected=SdIdRemoteException.class) 
	public void testRequestAuthenticationFails() throws Exception {
		new Expectations() {{
			sdid.requestAuthentication("billy", "SD-Store:123456789".getBytes()); 
			result = new WebServiceException();	
		}};	
		
		createSdIdClient().requestAuthentication("billy", "hunter2".getBytes());
	}
	
	@Test(expected=AuthenticationException.class) 
	public void testRequestAuthenticationEmpty() throws Exception {
		AuthReqFailed fault = new AuthReqFailed();
		fault.setReserved("boom".getBytes());
		
		new Expectations() {{
			sdid.requestAuthentication("", "SD-Store:123456789".getBytes()); 
			result = new AuthReqFailed_Exception("boom", fault);	
		}};	
		
		createSdIdClient().requestAuthentication("", "hunter2".getBytes());
	}
	
	@Test(expected=AuthenticationException.class) 
	public void testRequestAuthenticationNull() throws Exception {
		AuthReqFailed fault = new AuthReqFailed();
		fault.setReserved("boom".getBytes());
		
		new Expectations() {{
			sdid.requestAuthentication(null, "SD-Store:123456789".getBytes()); 
			result = new AuthReqFailed_Exception("boom", fault);	
		}};	
		
		createSdIdClient().requestAuthentication(null, "hunter2".getBytes());
	}
	

}
