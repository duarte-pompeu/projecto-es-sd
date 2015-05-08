package pt.ulisboa.tecnico.sdis.store.tests;

import static org.junit.Assert.*;

import java.util.Collection;

import javax.naming.directory.InvalidAttributeValueException;
import javax.xml.registry.JAXRException;

import org.junit.BeforeClass;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.cli.ClientFrontEnd;
import pt.ulisboa.tecnico.sdis.store.cli.StoreClient;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class FrontEndRemoteTest {
	static ClientFrontEnd frontEnd;
	
	public static final String USER = "duarte";
	public static final String DOC_1 = "SD for dummies";
	public static final String DOC_2 = "Top UDDI names for 2015";
	public static final String CONTENT = "Em SD, o importante é distribuir o jogo.";
	
	@BeforeClass
	public static void populate() throws JAXRException{
		frontEnd = new ClientFrontEnd();
		try {
			frontEnd.createDoc(USER, DOC_1);
			frontEnd.createDoc(USER, DOC_2);
		} catch (InvalidAttributeValueException | DocAlreadyExists_Exception e) {
			// lets not do anything
		}
		
	}
	
	@Test
	public void popSuccess() throws InvalidAttributeValueException, UserDoesNotExist_Exception{
		Collection<String> docs = frontEnd.listDocs(USER);
		
		assertTrue(docs.contains(DOC_1));
		assertTrue(docs.contains(DOC_2));
		
		// there can be more docs from other tests
		// but there shouldn't be less than 2 docs -> we've just added them
		// unless we reach the limit of storage capacity
		assertTrue(docs.size() >= 2);
	}
	
	@Test
	public void storeAndLoad() throws InvalidAttributeValueException, CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception{
		byte[] cont = StoreClient.string2bytes(CONTENT);
		
		frontEnd.storeDoc(USER, DOC_1, cont);
		
		byte[] result = frontEnd.loadDoc(USER, DOC_1);
		String resultsr = StoreClient.bytes2string(result);
		
		assertEquals(CONTENT, resultsr);
	}
}
