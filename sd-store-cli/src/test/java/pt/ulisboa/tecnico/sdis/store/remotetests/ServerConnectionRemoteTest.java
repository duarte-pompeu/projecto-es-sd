package pt.ulisboa.tecnico.sdis.store.remotetests;

import static org.junit.Assert.assertTrue;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;
import javax.naming.directory.InvalidAttributeValueException;
import javax.xml.registry.JAXRException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.cli.StoreClient;
import pt.ulisboa.tecnico.sdis.store.cli.service.CreateDocService;
import pt.ulisboa.tecnico.sdis.store.cli.service.ListDocsService;
import pt.ulisboa.tecnico.sdis.store.cli.service.LoadDocService;
import pt.ulisboa.tecnico.sdis.store.cli.service.StoreDocService;
import pt.ulisboa.tecnico.sdis.store.localtests.SDStoreClientTest;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class ServerConnectionRemoteTest  extends SDStoreClientTest{
	private final String USER = "duarte";
	private final String DOC_STORED = "my first novel";
	private final String DOC_NOT_STORED = "my second novel";
	private final String CONTENT = "This is a short story. THE END.";
	
	
	@BeforeClass
	public static void connect2server() throws JAXRException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException{
		StoreClient cli = new StoreClient();
		_port = cli.getPort();
	}
	
	
	@Before
	public void populate4Test() throws InvalidAttributeValueException{
		// make sure there's at least one doc in server
		CreateDocService service = new CreateDocService(USER, DOC_STORED, getPort());
		try {
			service.dispatch();
		} catch (DocAlreadyExists_Exception e) {
			return;
		}
	}
	
	
	@Test
	/**
	 * Requires a successful connection with the storage server
	 */
	public void populateSuccess() throws UserDoesNotExist_Exception, InvalidAttributeValueException{
		ListDocsService service = new ListDocsService(USER, getPort());
		service.dispatch();
		
		assertTrue(service.getResult().contains(DOC_STORED));
	}
	
	/**
	 * Uses a bad port. The test should fail.
	 */
	@Test (expected=InvalidAttributeValueException.class)
	public void createNullPort() throws DocAlreadyExists_Exception, InvalidAttributeValueException{
		CreateDocService service = new CreateDocService(USER, DOC_NOT_STORED, null);
		service.dispatch();
	}
	

	/**
	 * Uses a bad port. The test should fail.
	 */
	@Test (expected=InvalidAttributeValueException.class)
	public void listNullPort() throws UserDoesNotExist_Exception, InvalidAttributeValueException{
		ListDocsService service = new ListDocsService(USER, null);
		service.dispatch();
	}
	

	/**
	 * Uses a bad port. The test should fail.
	 */
	@Test (expected=InvalidAttributeValueException.class)
	public void storeNullPort() throws CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception, InvalidAttributeValueException{
		StoreDocService service = new StoreDocService(USER, DOC_STORED, string2bytes(CONTENT), null);
		service.dispatch();
	}
	

	/**
	 * Uses a bad port. The test should fail.
	 */
	@Test (expected=InvalidAttributeValueException.class)
	public void loadNullPort() throws DocDoesNotExist_Exception, UserDoesNotExist_Exception, InvalidAttributeValueException{
		LoadDocService service = new LoadDocService(USER, DOC_STORED, null);
		service.dispatch();
	}
}
