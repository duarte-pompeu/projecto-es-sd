package pt.ulisboa.tecnico.sdis.store.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import javax.naming.directory.InvalidAttributeValueException;

import mockit.Expectations;
import mockit.Mocked;

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

import com.sun.istack.NotNull;

/**
 * These tests validate the Client services without a server dependency.
 * To accomplish this, mocks are used to emulate server behaviour.
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
			 	DocUserPair dup = new DocUserPair();
				dup.setUserId(USER);
				dup.setDocumentId(DOC);	
				
				// FIXME: check if pair has right params
				mockPort.createDoc((DocUserPair) withNotNull()); times = 1;
		}};
			
		CreateDocService create = new CreateDocService(USER, DOC, mockPort);
		create.dispatch();
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
		 	DocUserPair dup = new DocUserPair();
			dup.setUserId(USER);
			dup.setDocumentId(DOC);	
			
			//FIXME: check dup params
			mockPort.store((DocUserPair) withNotNull(), string2bytes(CONTENT)); times = 1;
		}};
		
		StoreDocService store = new StoreDocService(USER, DOC, string2bytes(CONTENT), mockPort);
		store.dispatch();
	}
	
	
	@Test
	public void loadDoc() throws InvalidAttributeValueException, DocDoesNotExist_Exception, UserDoesNotExist_Exception{
		
		new Expectations() {{
			DocUserPair dup = new DocUserPair();
			dup.setUserId(USER);
			dup.setDocumentId(DOC);
			
			// FIXME: check dup params
			mockPort.load((DocUserPair) withNotNull()); times = 1;
			result = string2bytes(CONTENT);
		}};
		
		LoadDocService load = new LoadDocService(USER, DOC, mockPort);
		load.dispatch();
		
		assertEquals(CONTENT, bytes2string(load.getResult()));
	}
	
}
