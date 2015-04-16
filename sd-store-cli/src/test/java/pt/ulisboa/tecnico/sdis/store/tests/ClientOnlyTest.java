package pt.ulisboa.tecnico.sdis.store.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import javax.naming.directory.InvalidAttributeValueException;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.cli.service.CreateDocService;
import pt.ulisboa.tecnico.sdis.store.cli.service.ListDocsService;
import pt.ulisboa.tecnico.sdis.store.cli.service.LoadDocService;
import pt.ulisboa.tecnico.sdis.store.cli.service.StoreDocService;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

/**
 * These tests validate the Client services without a server dependency.
 * To accomplish this, mocks are used to emulate server behaviour.
 * 
 * Expectations are used to:
 * 	1. Apply rules to parameters (ex: must not be null)
 *  2. Mock fake results (ex: return a string to mock a server's return message)
 *  
 * Verifications are used to analyse parameters with more detail.
 * Sometimes DocUserPair objects must be inspected, which is hard using only Expectations().
 * So, Verifications() and withCapture() are used to capture the argument 
 * and check its attributes or use its methods to infer state.
 */
public class ClientOnlyTest extends ClientTest {
	public static final String USER = "duarte";
	public static final String DOC = "SD-notes";
	public static final String CONTENT = "RPC, RMI and WS.";
	public static final int DEFAULT_MAX_CAP = 10 * 1024;
	
	@Mocked
	SDStore mockPort;
	
	@Test
	public void createDoc() throws InvalidAttributeValueException, DocAlreadyExists_Exception{
		
		new Expectations() {{	
			mockPort.createDoc((DocUserPair) withNotNull()); times = 1;
		}};
		
		
		CreateDocService create = new CreateDocService(USER, DOC, mockPort);
		create.dispatch();
		
		
		new Verifications() {{
			DocUserPair dup;
			mockPort.createDoc(dup = withCapture()); times = 1;
			
			assertNotNull(dup);
			assertEquals(USER, dup.getUserId());
			assertEquals(DOC, dup.getDocumentId());
		}};
	}
	
	@Test
	public void listDocs() throws InvalidAttributeValueException, UserDoesNotExist_Exception{
		
		new Expectations() {{
			mockPort.listDocs(USER); times = 1;
			
			ArrayList<String> mockResult = new ArrayList<String>();
			mockResult.add(USER);
			result = mockResult;
		}};
		
		
		ListDocsService list = new ListDocsService(USER, mockPort);
		list.dispatch();
		
		
		assertTrue(list.getResult().contains(USER));
	}
	
	
	@Test
	public void storeDoc() throws InvalidAttributeValueException, CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception{
		
		new Expectations() {{
			mockPort.store((DocUserPair) withNotNull(), string2bytes(CONTENT)); times = 1;
		}};
		
		
		StoreDocService store = new StoreDocService(USER, DOC, string2bytes(CONTENT), mockPort);
		store.dispatch();
		
		
		new Verifications() {{
			DocUserPair dup;
			byte[] bytes;
			mockPort.store(dup = withCapture(), bytes = withCapture()); times = 1;
			
			assertNotNull(dup);
			assertEquals(USER, dup.getUserId());
			assertEquals(DOC, dup.getDocumentId());
			
			assertNotNull(bytes);
			assertEquals(CONTENT, bytes2string(bytes));
		}};
	}
	
	
	@Test
	public void loadDoc() throws InvalidAttributeValueException, DocDoesNotExist_Exception, UserDoesNotExist_Exception{
		
		new Expectations() {{
			mockPort.load((DocUserPair) withNotNull()); times = 1;
			result = string2bytes(CONTENT);
		}};
		
		LoadDocService load = new LoadDocService(USER, DOC, mockPort);
		load.dispatch();
		
		assertEquals(CONTENT, bytes2string(load.getResult()));
		
		new Verifications() {{
			DocUserPair dup;
			mockPort.load(dup = withCapture()); times = 1;
			
			assertNotNull(dup);
			assertEquals(USER, dup.getUserId());
			assertEquals(DOC, dup.getDocumentId());
		}};
	}
	
}
