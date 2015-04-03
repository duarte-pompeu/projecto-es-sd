package pt.ulisboa.tecnico.sdis.store.ws;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.service.CreateDocService;

public class StorageTest extends StoreTest {
	Storage storage;
	
	@Override
	public void populate4Test(){
		storage = SDStoreMain.getStorage();
	}
	
	@Override
	public void destroy(){
		reset(storage);
	}
	
	@Test
	public void startsEmpty(){
		assertTrue(storage.getUsers().size() == 0);
		assertTrue(storage.getAllDocs().size() == 0);
	}
	
	@Test
	public void addDocs() throws DocAlreadyExists_Exception{
		ArrayList<CreateDocService> services = new ArrayList<CreateDocService>();
		
		services.add(new CreateDocService("jubi", "emails"));
		services.add(new CreateDocService("grupo", "email com anexo"));
		services.add(new CreateDocService("grupo", "email sem anexo"));
		
		for(CreateDocService s: services){
			s.dispatch();
		}
		
		assertTrue(storage.getUsers().size() == 2);
		assertTrue(storage.getAllDocs().size() == 3);
	}
	
	@Test (expected = DocAlreadyExists_Exception.class)
	public void addRepeatedDoc(){
		
		try{
			CreateDocService service = new CreateDocService("jubi", "emails");
			service.dispatch();
			service.dispatch();
		}
		
		catch(DocAlreadyExists_Exception e){
			
		}
	}
}
