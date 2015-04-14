package pt.ulisboa.tecnico.sdis.store.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.naming.directory.InvalidAttributeValueException;
import javax.xml.registry.JAXRException;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.cli.service.CreateDocService;
import pt.ulisboa.tecnico.sdis.store.cli.service.ListDocsService;
import pt.ulisboa.tecnico.sdis.store.cli.service.LoadDocService;
import pt.ulisboa.tecnico.sdis.store.cli.service.StoreDocService;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class ClientServiceTests extends ClientTest {
	String USER = "duarte";
	String DOC = "SD-notes";
	String CONTENT = "RPC, RMI and WS.";
	
	public ClientServiceTests() throws JAXRException{
		super();
	}
	
	@Override
	public void populate4Test(){
		CreateDocService create = new CreateDocService(USER, DOC, getPort());
		StoreDocService store = new StoreDocService(USER, DOC, string2bytes(CONTENT), getPort());
		
		try {
			create.dispatch();
			store.dispatch();
		} 
		catch (InvalidAttributeValueException | DocAlreadyExists_Exception
				| CapacityExceeded_Exception | DocDoesNotExist_Exception
				| UserDoesNotExist_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	@Test
	public void testPopulate() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception{
		ListDocsService list = new ListDocsService(USER, getPort());
		list.dispatch();
		assertTrue(list.getResult().contains(DOC));
		
		LoadDocService load = new LoadDocService(USER, DOC, getPort());
		load.dispatch();
		assertEquals(CONTENT, bytes2string(load.getResult()));
	}
	
	
	@Test (expected = DocAlreadyExists_Exception.class)
	public void repeatDoc() throws InvalidAttributeValueException, DocAlreadyExists_Exception{
		CreateDocService create = new CreateDocService(USER, DOC, getPort());
		create.dispatch();
	}
	
	
	@Test (expected = UserDoesNotExist_Exception.class)
	public void noUser() throws UserDoesNotExist_Exception, InvalidAttributeValueException {
		String badUser = "Hello, I'm root and I want all the documents.";
		ListDocsService service = new ListDocsService(badUser, getPort());
		service.dispatch();
	}
	
	@Test
	public void nearCapacityLimit() throws InvalidAttributeValueException, CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception {
		String content = "";
		String tmpUser = "alice";
		String tempDoc = "lista de compras";
		
		for(int i = 0; i < (10 * 1024); i++){
			content += "d";
		}
		
		byte [] bytes = string2bytes(content);
		
		assertEquals(10*1024, bytes.length);
		ListDocsService list = new ListDocsService(tmpUser, getPort());
		list.dispatch();
		
		// make sure there are no other docs in the user repo
		assertEquals(1, list.getResult().size());
		
		CreateDocService create = new CreateDocService(tmpUser, tempDoc, getPort());
		StoreDocService store = new StoreDocService(tmpUser, tempDoc, string2bytes(content), getPort());
		
		try {
			create.dispatch();
		} catch ( DocAlreadyExists_Exception e) {
			//continue, its ok if doc already exists
		}
		
		
		store.dispatch();
	}
	
	@Test (expected = CapacityExceeded_Exception.class)
	public void longDoc() throws UserDoesNotExist_Exception, CapacityExceeded_Exception, DocDoesNotExist_Exception, InvalidAttributeValueException{
		String message = "Hello. I'd like to buy a new keyboard, my 'd' key is broken. Look:\n";
		
		for(int i = 0; i < 1024; i++){
			message += "dddddddddddddddddddddddd";
		}
		
		StoreDocService service = new StoreDocService(USER, DOC, string2bytes(message), getPort());
		service.dispatch();
	}
	
	@Test (expected = DocDoesNotExist_Exception.class)
	public void storeInBadDoc() throws InvalidAttributeValueException, CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception{
		StoreDocService service = new StoreDocService(USER, "the doc doesnt exist but I'm gonna stuff content there anyway", 
				string2bytes(CONTENT), getPort());
		service.dispatch();
	}
}
