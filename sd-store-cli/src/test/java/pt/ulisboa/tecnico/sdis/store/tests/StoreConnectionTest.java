package pt.ulisboa.tecnico.sdis.store.tests;

import static org.junit.Assert.assertTrue;

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
		} catch (DocAlreadyExists_Exception e) {
			return;
		}
	}
	
	
	@Test
	public void populateSuccess() throws UserDoesNotExist_Exception{
		ListDocsService service = new ListDocsService(USER, getPort());
		service.dispatch();
		
		assertTrue(service.getResult().contains(DOC_STORED));
	}
	
	
	@Test
	public void createNullPort() throws DocAlreadyExists_Exception{
		CreateDocService service = new CreateDocService(USER, DOC_NOT_STORED, null);
		service.dispatch();
	}
	
	
	@Test
	public void listNullPort() throws UserDoesNotExist_Exception{
		ListDocsService service = new ListDocsService(USER, null);
		service.dispatch();
	}
	
	
	@Test
	public void storeNullPort() throws CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception{
		StoreDocService service = new StoreDocService(USER, DOC_STORED, string2bytes(CONTENT), null);
		service.dispatch();
	}
	
	
	@Test
	public void loadNullPort() throws DocDoesNotExist_Exception, UserDoesNotExist_Exception{
		LoadDocService service = new LoadDocService(USER, DOC_STORED, null);
		service.dispatch();
	}
}
