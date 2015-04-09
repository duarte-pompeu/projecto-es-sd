package pt.ulisboa.tecnico.sdis.store.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.SDStoreMain;
import pt.ulisboa.tecnico.sdis.store.ws.Storage;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class ListDocsTest{
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
	
	@Test
	public void populateSuccess(){
		assertEquals(3, storage.getUsers().size());
		assertEquals(4, storage.getAllDocs().size());
	}
	
	@Test (expected = UserDoesNotExist_Exception.class)
	public void noUser() throws UserDoesNotExist_Exception {
		String badUser = "Hello, I'm root and I want all the documents.";
		ListDocsService service = new ListDocsService(badUser);
		service.dispatch();
	}
	
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
}
