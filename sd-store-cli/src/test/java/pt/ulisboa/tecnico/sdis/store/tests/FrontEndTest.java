package pt.ulisboa.tecnico.sdis.store.tests;

import javax.naming.directory.InvalidAttributeValueException;
import javax.xml.registry.JAXRException;

import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.cli.ClientFrontEnd;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;

public class FrontEndTest {
	ClientFrontEnd frontEnd;
	
	@Before
	public void populate() throws JAXRException, InvalidAttributeValueException, DocAlreadyExists_Exception{
		frontEnd = new ClientFrontEnd();
		frontEnd.createDoc("duarte", "asd");
		
	}
	
	@Test
	public void popSuccess(){
		
	}
}
