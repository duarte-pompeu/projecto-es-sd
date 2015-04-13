package pt.ulisboa.tecnico.sdis.store.tests;

import static org.junit.Assert.assertTrue;

import javax.naming.directory.InvalidAttributeValueException;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.cli.service.CreateDocService;
import pt.ulisboa.tecnico.sdis.store.cli.service.ListDocsService;
import pt.ulisboa.tecnico.sdis.store.cli.service.LoadDocService;
import pt.ulisboa.tecnico.sdis.store.cli.service.StoreDocService;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class StoreConnectionTest  extends ClientStoreServicesTest{
	private final String USER = "duarte";
	private final String DOC_STORED = "my first novel";
	private final String DOC_NOT_STORED = "my second novel";
	private final String CONTENT = "This is a short story. THE END.";
	
	public StoreConnectionTest(){
		super();
	}
	
	@Override
	public void populate4Test(){
		CreateDocService service = new CreateDocService(USER, DOC_STORED, getPort());
		try {
			service.dispatch();
		} catch (Exception e) {
			return;
		}
	}
	
	
	@Test
	// requires a successful connection with the storage server
	public void populateSuccess() throws UserDoesNotExist_Exception, InvalidAttributeValueException{
		ListDocsService service = new ListDocsService(USER, getPort());
		service.dispatch();
		
		assertTrue(service.getResult().contains(DOC_STORED));
	}
	
	
	@Test (expected=InvalidAttributeValueException.class)
	public void createNullPort() throws DocAlreadyExists_Exception, InvalidAttributeValueException{
		CreateDocService service = new CreateDocService(USER, DOC_NOT_STORED, null);
		service.dispatch();
	}
	
	
	@Test (expected=InvalidAttributeValueException.class)
	public void listNullPort() throws UserDoesNotExist_Exception, InvalidAttributeValueException{
		ListDocsService service = new ListDocsService(USER, null);
		service.dispatch();
	}
	
	
	@Test (expected=InvalidAttributeValueException.class)
	public void storeNullPort() throws CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception, InvalidAttributeValueException{
		StoreDocService service = new StoreDocService(USER, DOC_STORED, string2bytes(CONTENT), null);
		service.dispatch();
	}
	
	
	@Test (expected=InvalidAttributeValueException.class)
	public void loadNullPort() throws DocDoesNotExist_Exception, UserDoesNotExist_Exception, InvalidAttributeValueException{
		LoadDocService service = new LoadDocService(USER, DOC_STORED, null);
		service.dispatch();
	}
}
