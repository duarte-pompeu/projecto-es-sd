package pt.ulisboa.tecnico.sdis.store.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.SDStoreMain;
import pt.ulisboa.tecnico.sdis.store.ws.Storage;

public class CreateDocTest {
	Storage storage;
	
	@Before
	public void populate4Test(){
		storage = SDStoreMain.getStorage();
	}
	
	@After
	public void destroy(){
		storage.init();
	}
	
	@Test
	public void startsEmpty(){
		assertEquals(0, storage.getUsers().size());
		assertEquals(0, storage.getAllDocs().size());
	}
	
	@Test
	public void addDocs() throws DocAlreadyExists_Exception{
		ArrayList<CreateDocService> services = new ArrayList<CreateDocService>();
		
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
	
	@Test (expected = DocAlreadyExists_Exception.class)
	public void addRepeatedDoc(){
		
		try{
			CreateDocService service1 = new CreateDocService("jubi", "emails");
			service1.dispatch();
			service1.dispatch();
		}
		
		catch(DocAlreadyExists_Exception e){
			throw new RuntimeException();
		}
	}
}
