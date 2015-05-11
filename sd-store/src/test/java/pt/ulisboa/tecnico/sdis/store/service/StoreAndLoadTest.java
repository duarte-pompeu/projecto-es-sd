package pt.ulisboa.tecnico.sdis.store.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.SDStoreMain;
import pt.ulisboa.tecnico.sdis.store.ws.Storage;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class StoreAndLoadTest extends ServerTest {
	String U1 = "jubi";
	String U1D1 = "tuta mail";
	String U1D1C1 = "de: jubi\npara: leo\nboas, tudo bem?";
	public static final int DEFAULT_MAX_CAP = 10*1024;
	
	Storage storage;
	
	@Before
	public void populate4Test(){
		storage = SDStoreMain.getStorage();
		ArrayList<CreateDocService> services = new ArrayList<CreateDocService>();
		
		storage.createCollection(U1);
		services.add(new CreateDocService(U1, U1D1));

		
		try{
			for(CreateDocService s: services){
				s.dispatch();
			}
		} catch(DocAlreadyExists_Exception e){
		
		}
	}
	
	@After
	public void destroy(){
		storage.init();
	}
	
	@Test
	public void populateSuccess(){
		assertEquals(1, storage.getUsers().size());
		assertEquals(1, storage.getAllDocs().size());
	}
	
	/** 
	 * storeSuccess() makes sure the storage initial status is what we expect.
	 * If this test fails -> storage initial status is not ok -> other tests may fail (due to their exceptions on the initial status)
	 */
	@Test
	public void storeSuccess() throws UserDoesNotExist_Exception, CapacityExceeded_Exception, DocDoesNotExist_Exception{
		StoreService service1 = new StoreService(U1, U1D1, string2bytes(U1D1C1));
		service1.dispatch();
		
		assertEquals(1, storage.getUsers().size());
		assertEquals(1, storage.getAllDocs().size());
		
		LoadService service2 = new LoadService(U1, U1D1);
		service2.dispatch();
		
		// check if we have the same number of users and docs
		assertEquals(1, storage.getUsers().size());
		assertEquals(1, storage.getAllDocs().size());
		
		// check if content loaded equals what we stored
		assertEquals(U1D1C1, bytes2string(service2.getResult()));
	}
	
	
	/** 
	 * noUser tries to store content in a repo that doesn't exist.
	 * It should raise an exception.
	 */
	@Test (expected = UserDoesNotExist_Exception.class)
	public void noUser() throws UserDoesNotExist_Exception, CapacityExceeded_Exception, DocDoesNotExist_Exception{
		StoreService service1 = new StoreService("anonymous", U1D1, string2bytes(U1D1C1));
		service1.dispatch();
	}
	
	
	/** 
	 * noDoc() tries to store content on a good repo but bad doc (doc doesn't exist).
	 * It should raise an exception.
	 */
	@Test (expected = DocDoesNotExist_Exception.class)
	public void noDoc() throws UserDoesNotExist_Exception, CapacityExceeded_Exception, DocDoesNotExist_Exception{
		StoreService service = new StoreService(U1, "the doc doesnt exist but I'm gonna stuff content there anyway", string2bytes(U1D1C1));
		service.dispatch();
	}
	
	
	/**
	 * edgeOfCapacity() tries to store content in a doc right at the limit (10*1024 bytes).
	 * It should succeed with no exception.
	 */
	@Test
	public void edgeOfCapacity() throws UserDoesNotExist_Exception, CapacityExceeded_Exception, DocDoesNotExist_Exception{
		String message = new String();
		int size = DEFAULT_MAX_CAP;
		
		for(int i = 0; i < size; i++){
			message += "d";
		}
		
		StoreService service1 = new StoreService(U1, U1D1, string2bytes(message));
		service1.dispatch();
	}
	
	
	/**
	 * longDoc() tries to store content in a doc right ABOVE the limit (10*1024 bytes).
	 * It should raise an exception.
	 */
	@Test (expected = CapacityExceeded_Exception.class)
	public void longDoc() throws UserDoesNotExist_Exception, CapacityExceeded_Exception, DocDoesNotExist_Exception{
		String message = new String();
		int size = DEFAULT_MAX_CAP + 1;
		
		for(int i = 0; i < size; i++){
			message += "d";
		}
		
		StoreService service1 = new StoreService(U1, U1D1, string2bytes(message));
		service1.dispatch();
	}
	
	/**
	 * edgeOfCapacityInc() will storage multiple times content incrementally longer.
	 * The final content size will match exactly the default capacity size.
	 * The test should succeed and not raise any exception.
	 */
	@Test
	public void edgeOfCapacityInc() throws UserDoesNotExist_Exception, CapacityExceeded_Exception, DocDoesNotExist_Exception{
		String message = new String();
		int size = DEFAULT_MAX_CAP;
		
		for(int i = 0; i < size; i++){
			message += "d";
			
			StoreService service1 = new StoreService(U1, U1D1, string2bytes(message));
			service1.dispatch();
		}
		
		assertEquals(size, message.length());
		assertEquals(size, string2bytes(message).length);
		
	}
	
	/**
	 * longDocInc() will storage multiple times content incrementally longer.
	 * The final content size will be 1 byte above the limit.
	 * The test should raise an exception.
	 */
	@Test (expected = CapacityExceeded_Exception.class)
	public void longDocInc() throws UserDoesNotExist_Exception, CapacityExceeded_Exception, DocDoesNotExist_Exception{
		String message = new String();
		int size = DEFAULT_MAX_CAP;
		
		for(int i = 0; i < size; i++){
			message += "d";
			
			StoreService service1 = new StoreService(U1, U1D1, string2bytes(message));
			service1.dispatch();
		}
		
		assertEquals(size, message.length());
		
		message += "d";
		StoreService service1 = new StoreService(U1, U1D1, string2bytes(message));
		service1.dispatch();
	}
	
	
	/**
	 * storeLoadRepeat() will store a document multiple times.
	 * Each time, it will also load a document and check if the content is as expected.
	 * The test should succeed.
	 */
	@Test
	public void storeLoadRepeat()throws UserDoesNotExist_Exception, CapacityExceeded_Exception, DocDoesNotExist_Exception{
		String[] email_edits = { "ola", "oi", "boas", "boa tarde", "não sei como começar o email, vou guardar como rascunho" };
		
		StoreService store;
		LoadService load;
		
		for(String s: email_edits){
			store = new StoreService(U1, U1D1, string2bytes(s));
			store.dispatch();
			
			load = new LoadService(U1, U1D1);
			load.dispatch();
			String result = bytes2string(load.getResult());
			
			assertEquals(s, result);
		}
		
		assertEquals(1, storage.getUsers().size());
		assertEquals(1, storage.getAllDocs().size());
	}
	
	@Test
	public void assertVersions() throws UserDoesNotExist_Exception, CapacityExceeded_Exception, DocDoesNotExist_Exception, DocAlreadyExists_Exception{
		String doc = "temp";
		
		String[] email_edits = { "ola", "oi", "boas", "boa tarde", "não sei como começar o email, vou guardar como rascunho" };
		
		CreateDocService create = new CreateDocService(U1, doc);
		create.dispatch();
		
		int version = storage.getCollection(U1).getDoc(doc).getVersion();
		assertEquals(0, version);
		
		StoreService store;
		String s;
		for(int i = 0; i < email_edits.length; i++){
			s = email_edits[i];
			store = new StoreService(U1, doc, string2bytes(s));
			store.dispatch();
			
			version = storage.getCollection(U1).getDoc(doc).getVersion();
			assertEquals(i+1, version);
		}
	}
}
