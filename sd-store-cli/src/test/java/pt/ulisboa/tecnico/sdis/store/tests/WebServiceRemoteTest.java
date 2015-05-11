package pt.ulisboa.tecnico.sdis.store.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.naming.directory.InvalidAttributeValueException;
import javax.xml.registry.JAXRException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.cli.StoreClient;
import pt.ulisboa.tecnico.sdis.store.cli.service.StoreDocService;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
/**
 * Most of these tests require a connection with a server. Preferably one with a clean state - nothing stored in it.
 */
public class WebServiceRemoteTest extends SDStoreClientTest {
	public static final String USER = "duarte";
	public static final String DOC = "SD-notes";
	public static final String CONTENT = "RPC, RMI and WS.";
	public static final int DEFAULT_MAX_CAP = 10 * 1024;
	static StoreClient client;
	
	@BeforeClass
	public static void connect2server() throws JAXRException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException{
		client = new StoreClient();
		_port = client.getPort();
	}
	
	
	@Before
	public void populate4Test() throws InvalidAttributeValueException, CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception{
		
		try {
			client.createDoc(USER, DOC);
		}
		
		catch(DocAlreadyExists_Exception e){
			// that's fine
		}
		
		client.storeDoc(USER, DOC, string2bytes(CONTENT));
	}
	
	
	@Test
	public void testPopulate() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, IllegalBlockSizeException, BadPaddingException{
		List<String> result = client.listDocs(USER);
		assertTrue(result.contains(DOC));
		
		byte[] plainBytes = client.loadDoc(USER, DOC);
		String content = bytes2string(plainBytes);
		
		assertEquals(CONTENT, content);
	}
	
	
	@Test (expected = DocAlreadyExists_Exception.class)
	public void repeatDoc() throws InvalidAttributeValueException, DocAlreadyExists_Exception{
		client.createDoc(USER, DOC);
	}
	
	
	@Test (expected = UserDoesNotExist_Exception.class)
	public void noUser() throws UserDoesNotExist_Exception, InvalidAttributeValueException {
		client.listDocs("Hello, I'm a hacker and I want all the documents.");
	}
	
	
	@Test
	public void nearCapacityLimit() throws InvalidAttributeValueException, CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception {
		// encryption changes the doc size
		// since we wanna test this, let's turn it off for a while
		// please don't try to decrypt unencrypted docs later
		client.setEncryption(false);
		
		String content = "";
		String tmpUser = "alice";
		String tempDoc = "lista de compras";
		int size = DEFAULT_MAX_CAP;
		
		for(int i = 0; i < size; i++){
			content += "d";
		}
		
		byte [] bytes = string2bytes(content);
		
		assertEquals(size, bytes.length);
		client.listDocs(tmpUser);
		
		try {
			client.createDoc(tmpUser, tempDoc);
		} catch ( DocAlreadyExists_Exception e) {
			//continue, its ok if doc already exists
		}
		
		client.storeDoc(tmpUser, tempDoc, string2bytes(content));
		
		client.setEncryption(true);
	}
	
	
	@Test (expected = DocDoesNotExist_Exception.class)
	public void storeInBadDoc() throws InvalidAttributeValueException, CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception{
		client.storeDoc(USER, "the doc doesnt exist but I'm gonna stuff content there anyway", string2bytes(CONTENT));
	}
	
	//FIXME the server throws a really stupid exception when MAC keys are invalid
	@Test (expected = UserDoesNotExist_Exception.class)
	public void badMAC() throws InvalidAttributeValueException, CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception{
		SDStore port = client.getPort();
		StoreDocService service = new StoreDocService(USER, DOC, string2bytes(CONTENT), port);
		
		byte [] badContent = string2bytes("FAAAAAAAAKE");
		service.addMacDigest(badContent);
		
		service.dispatch();
	}
}
