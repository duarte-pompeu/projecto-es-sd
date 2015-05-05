package pt.ulisboa.tecnico.sdis.store.tests;

import static org.junit.Assert.*;

import java.util.Collection;

import javax.naming.directory.InvalidAttributeValueException;
import javax.xml.registry.JAXRException;

import org.junit.BeforeClass;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.cli.ClientFrontEnd;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class FrontEndTest {
	static ClientFrontEnd frontEnd;
	
	public static final String USER = "duarte";
	public static final String DOC_1 = "SD for dummies";
	public static final String DOC_2 = "Top UDDI names for 2015";
	
	@BeforeClass
	public static void populate() throws JAXRException, InvalidAttributeValueException{
		frontEnd = new ClientFrontEnd();
		frontEnd.createDoc(USER, DOC_1);
		frontEnd.createDoc(USER, DOC_2);
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
}
