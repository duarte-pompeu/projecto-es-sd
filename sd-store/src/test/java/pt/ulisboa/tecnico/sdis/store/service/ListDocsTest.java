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

public class ListDocsTest extends ServerTest{
	Storage storage;
	
	String U1 = "jubi";
	String U1D1 = "email";
	
	String U2 = "grupo1";
	String U2D1 = "email com anexo";
	String U2D2 = "email sem anexo";
	
	String U3 = "grupo2";
	String U3D1 = U1D1;
	
	@Before
	public void populate4Test(){
		storage = SDStoreMain.getStorage();
		ArrayList<CreateDocService> services = new ArrayList<CreateDocService>();
		
		storage.createCollection(U1);
		storage.createCollection(U2);
		storage.createCollection(U3);
		
		services.add(new CreateDocService(U1, U1D1));
		services.add(new CreateDocService(U2, U2D1));
		services.add(new CreateDocService(U2, U2D2));
		services.add(new CreateDocService(U3, U3D1));
		
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
	
	/**
	 * Assert number of users and number of docs is as expected after populate.
	 */
	@Test
	public void populateSuccess(){
		assertEquals(3, storage.getUsers().size());
		assertEquals(4, storage.getAllDocs().size());
	}
	
	
	/**
	 * Try to list docs from a not registered user.
	 */
	@Test (expected = UserDoesNotExist_Exception.class)
	public void noUser() throws UserDoesNotExist_Exception {
		String badUser = "I dont have a repo but I wanna list my docs.";
		ListDocsService service = new ListDocsService(badUser);
		service.dispatch();
	}
	
	
	/**
	 * List docs from existing users.
	 * This test should pass.
	 */
	@Test
	public void listDocs()
		throws UserDoesNotExist_Exception{
		ArrayList<ListDocsService> services = new ArrayList<ListDocsService>();
		
		services.add(new ListDocsService(U1));
		services.add(new ListDocsService(U2));
		services.add(new ListDocsService(U3));
		
		for(ListDocsService service: services){		
			service.dispatch();
		}

		// U1 - 1 document
		assertEquals(1, services.get(0).getResult().size());
		assertEquals(U1D1, services.get(0).getResult().get(0));
		
		// U2 - 2 documents
		assertEquals(2, services.get(1).getResult().size());
		assertEquals(U2D1, services.get(1).getResult().get(0));
		assertEquals(U2D2, services.get(1).getResult().get(1));
		
		// U3 - 1 document
		assertEquals(1, services.get(2).getResult().size());
		assertEquals(U3D1, services.get(2).getResult().get(0));
	}
	
	@Test
	public void testSeq() throws UserDoesNotExist_Exception, CapacityExceeded_Exception, DocDoesNotExist_Exception{
		//created 1 doc - seq for repo is 1
		assertEquals(1, storage.getCollection(U1).getWriteCount());
		//created 2 doc - seq for repo is 1
		assertEquals(2, storage.getCollection(U2).getWriteCount());
		//created 1 doc - seq for repo is 1
		assertEquals(1, storage.getCollection(U3).getWriteCount());
		
		StoreService store = new StoreService(U2, U2D1, string2bytes("teste"));
		store.dispatch();
		
		//doc version should be 1
		assertEquals(1,store.seq);
		//repo version should be initial + 1 = 2 + 1 = 3
		assertEquals(3, storage.getCollection(U2).getWriteCount());
	}
}
