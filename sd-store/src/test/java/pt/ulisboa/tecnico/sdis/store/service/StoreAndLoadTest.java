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

public class StoreAndLoadTest {
	String U1 = "jubi";
	String U1D1 = "tuta mail";
	String U1D1C1 = "de: jubi\npara: leo\nboas, tudo bem?";
	
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
	
	@Test (expected = UserDoesNotExist_Exception.class)
	public void noUser() throws UserDoesNotExist_Exception, CapacityExceeded_Exception, DocDoesNotExist_Exception{
		StoreService service1 = new StoreService("anonymous", U1D1, string2bytes(U1D1C1));
		service1.dispatch();
	}
	
	
	@Test (expected = DocDoesNotExist_Exception.class)
	public void noDoc() throws UserDoesNotExist_Exception, CapacityExceeded_Exception, DocDoesNotExist_Exception{
		StoreService service = new StoreService(U1, "the doc doesnt exist but I'm gonna stuff content there anyway", string2bytes(U1D1C1));
		service.dispatch();
	}
	
	
	@Test (expected = CapacityExceeded_Exception.class)
	public void longDoc() throws UserDoesNotExist_Exception, CapacityExceeded_Exception, DocDoesNotExist_Exception{
		String message = "Hello. I'd like to buy a new keyboard, my 'd' key is broken. Look:\n";
		
		for(int i = 0; i < 1000; i++){
			message += "dddddddddddddddddddddddd";
		}
		
		StoreService service1 = new StoreService(U1, U1D1, string2bytes(message));
		service1.dispatch();
	}
	
	
	@Test (expected = CapacityExceeded_Exception.class)
	public void docGetsIncrementallyBigger() throws UserDoesNotExist_Exception, CapacityExceeded_Exception, DocDoesNotExist_Exception{
		String message = "Hello. I'd like to buy a new keyboard, my 'd' key is broken. Look:\n";
		
		for(int i = 0; i < 1000; i++){
			message += "dddddddddddddddddddddddd";
			
			StoreService service1 = new StoreService(U1, U1D1, string2bytes(message));
			service1.dispatch();
		}
	}
	
	
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
	
	
	public byte[] string2bytes(String s){
		return SDStoreMain.string2bytes(s);
	}
	
	
	public String bytes2string(byte[] bytes){
		return SDStoreMain.bytes2string(bytes);
	}
}
