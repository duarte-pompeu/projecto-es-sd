package pt.ulisboa.tecnico.sdis.store.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import javax.naming.directory.InvalidAttributeValueException;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.cli.ClientFrontEnd;
import pt.ulisboa.tecnico.sdis.store.cli.StoreClient;
import pt.ulisboa.tecnico.sdis.store.exceptions.NoConsensusException;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class FrontEndLocalTest extends SDStoreClientTest {
	static ArrayList<StoreClient> _clients;
	static ClientFrontEnd _fe;
	
	static String USER = "Manuel";
	static String DOC = "documento";
	static byte[] CONTENT = string2bytes("TEST");
	
	@Mocked
	StoreClient mockCli1, mockCli2, mockCli3;
	
	@Before
	public void beforeTest(){
		_clients = new ArrayList<StoreClient>();
		
		_clients.add(mockCli1);
		_clients.add(mockCli2);
		_clients.add(mockCli3);
		
		_fe = new ClientFrontEnd(_clients);
	}
	
	
	@Test
	public void storeSuccess() 
			throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		
		new Expectations() {{
			mockCli1.storeDoc(USER, DOC, CONTENT);
			mockCli2.storeDoc(USER, DOC, CONTENT);
			mockCli3.storeDoc(USER, DOC, CONTENT);
		}};
		
		_fe.storeDoc(USER, DOC, CONTENT);
	}
	
	
	@Test (expected = UserDoesNotExist_Exception.class)
	public void storeException() 
			throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		
		String message = "ERROR!";
		UserDoesNotExist uddne = new UserDoesNotExist();
		uddne.setUserId("Manuel");
		final UserDoesNotExist_Exception UDNEex = new UserDoesNotExist_Exception(message, uddne);
		
		new Expectations() {{
			mockCli1.storeDoc(USER, DOC, CONTENT);
			result = CONTENT;
		}};
		
		new Expectations() {{
			mockCli2.storeDoc(USER, DOC, CONTENT);
			result = UDNEex;
		}};
		
		new Expectations() {{
			mockCli3.storeDoc(USER, DOC, CONTENT);
			result = UDNEex;
		}};
		
		_fe.storeDoc(USER, DOC, CONTENT);
	}
	
	
	@Test
	public void loadSuccess() 
			throws InvalidAttributeValueException, DocDoesNotExist_Exception, UserDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		
		new Expectations() {{
			mockCli1.loadDoc(USER, DOC);
			mockCli2.loadDoc(USER, DOC);
			mockCli3.loadDoc(USER, DOC);
			
			result = CONTENT;
		}};
		
		assertEquals(bytes2string(CONTENT), bytes2string(_fe.loadDoc(USER, DOC)));
	}
	
	
	@Test
	public void loadWithQuorumVotes() 
			throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		
		new Expectations() {{
			mockCli1.loadDoc(USER, DOC);
			result = CONTENT;
		}};
		
		new Expectations() {{
			mockCli2.loadDoc(USER, DOC);
			result = CONTENT;
		}};	
		
		new Expectations() {{
			mockCli3.loadDoc(USER, DOC);
			result = string2bytes("OOPS, WRONG CONTENT");
		}};
		
		assertEquals(bytes2string(CONTENT), bytes2string(_fe.loadDoc(USER, DOC)));
	}
	
	
	@Test (expected = DocDoesNotExist_Exception.class)
	public void quorumWithDifferentExceptions() 
			throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		
		String message = "ERROR!";
		UserDoesNotExist udne = new UserDoesNotExist();
		udne.setUserId("Manuel");
		DocDoesNotExist ddne = new DocDoesNotExist();
		ddne.setDocId("DOC");
		
		final UserDoesNotExist_Exception UDNEex = new UserDoesNotExist_Exception(message, udne);
		final DocDoesNotExist_Exception DDNEex = new DocDoesNotExist_Exception(message, ddne);
		
		new Expectations() {{
			mockCli1.loadDoc(USER, DOC);
			result = UDNEex;
		}};
		
		new Expectations() {{
			mockCli2.loadDoc(USER, DOC);
			result = DDNEex;
		}};	
		
		new Expectations() {{
			mockCli3.loadDoc(USER, DOC);
			result = DDNEex;
		}};
		
		_fe.loadDoc(USER, DOC);
	}
	
	
	@Test
	public void noConsensus() 
			throws InvalidAttributeValueException, DocDoesNotExist_Exception, UserDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		
		new Expectations() {{
			mockCli1.loadDoc(USER, DOC);
			result = string2bytes("HEY");
		}};
		
		new Expectations() {{
			mockCli2.loadDoc(USER, DOC);
			result = string2bytes("HELLO");
		}};	
		
		new Expectations() {{
			mockCli3.loadDoc(USER, DOC);
			result = string2bytes("BYE");
		}};
		
		_fe.loadDoc(USER, DOC);
	}
	
	@Test
	public void writeBack1() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		new Expectations() {{
			mockCli1.loadDoc(USER, DOC);
			result = CONTENT;
		}};
		
		new Expectations() {{
			mockCli2.loadDoc(USER, DOC);
			result = string2bytes("WRONG CONTENT");
		}};	
		
		new Expectations() {{
			mockCli3.loadDoc(USER, DOC);
			result = CONTENT;
		}};
		
		new Expectations() {{
			mockCli2.storeDoc(USER, DOC, CONTENT);
		}};
		
		byte[] result = _fe.loadDoc(USER, DOC);
		
		assertEquals(result, CONTENT);
	}
	
	@Test
	public void writeBack2() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		String message = "ERROR!";
		UserDoesNotExist uddne = new UserDoesNotExist();
		uddne.setUserId("Manuel");
		final UserDoesNotExist_Exception UDNEex = new UserDoesNotExist_Exception(message, uddne);
		
		new Expectations() {{
			mockCli1.loadDoc(USER, DOC);
			result = UDNEex;
		}};
		
		new Expectations() {{
			mockCli2.loadDoc(USER, DOC);
			result = CONTENT;
		}};	
		
		new Expectations() {{
			mockCli3.loadDoc(USER, DOC);
			result = CONTENT;
		}};
		
		new Expectations() {{
			mockCli1.storeDoc(USER, DOC, CONTENT);
		}};
		
		byte[] result = _fe.loadDoc(USER, DOC);
		assertEquals(result, CONTENT);
	}
}
