package pt.ulisboa.tecnico.sdis.store.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.SDStoreMain;
import pt.ulisboa.tecnico.sdis.store.ws.Storage;

public class CreateDocTest extends ServerTest {
	Storage storage;
	
	
	@Before
	public void populate4Test(){
		storage = SDStoreMain.getStorage();
	}
	
	
	@After
	public void destroy(){
		storage.init();
	}
	
	
	/**
	 * Assert storage starts empty.
	 */
	@Test
	public void startsEmpty(){
		assertEquals(0, storage.getUsers().size());
		assertEquals(0, storage.getAllDocs().size());
	}
	
	
	/**
	 * Create some repositories and add some docs.
	 * Finally, test if number of repos and number of docs is as expected.
	 */
	@Test
	public void addDocs() throws DocAlreadyExists_Exception{
		ArrayList<CreateDocService> services = new ArrayList<CreateDocService>();
		
		storage.createCollection("jubi");
		storage.createCollection("grupo1");
		storage.createCollection("grupo2");
		services.add(new CreateDocService("jubi", "email"));
		services.add(new CreateDocService("grupo1", "email com anexo"));
		services.add(new CreateDocService("grupo1", "email sem anexo"));
		services.add(new CreateDocService("grupo2", "email"));
		
		for(CreateDocService s: services){
			s.dispatch();
		}
		
		assertEquals(3, storage.getUsers().size());
		assertEquals(4, storage.getAllDocs().size());
	}
	
	
	/**
	 * Try to add a doc twice and raise an exception.
	 * This test doesn't depend on storage status:
	 * 		If doc doesn't exist, exception is raised on 2nd dispatch.
	 * 		If doc already exists, exception is raised on 1st dispatch.
	 */
	@Test (expected = DocAlreadyExists_Exception.class)
	public void addRepeatedDoc() throws DocAlreadyExists_Exception{
		storage.createCollection("jubi");
		
		CreateDocService service1 = new CreateDocService("jubi", "emails");
		service1.dispatch();
		
		CreateDocService service2 = new CreateDocService("jubi", "emails");
		service2.dispatch();
	}
}
