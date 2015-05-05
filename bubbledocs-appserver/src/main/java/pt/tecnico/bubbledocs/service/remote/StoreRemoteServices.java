package pt.tecnico.bubbledocs.service.remote;

import javax.naming.directory.InvalidAttributeValueException;
import javax.xml.registry.JAXRException;

import pt.tecnico.bubbledocs.exceptions.CannotLoadDocumentException;
import pt.tecnico.bubbledocs.exceptions.CannotStoreDocumentException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.sdis.store.cli.ClientFrontEnd;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class StoreRemoteServices {
	ClientFrontEnd frontEnd;
	
	public StoreRemoteServices(){
		try {
			frontEnd = new ClientFrontEnd();
		} catch (JAXRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void storeDocument(String username, String docName, byte[] document)
			throws CannotStoreDocumentException, RemoteInvocationException {
		
		try {
			frontEnd.storeDoc(username, docName, document);
		} catch (InvalidAttributeValueException | CapacityExceeded_Exception
				| DocDoesNotExist_Exception | UserDoesNotExist_Exception e) {
			
			throw new CannotStoreDocumentException(e.getMessage());
		}
	}
	public byte[] loadDocument(String username, String docName)
			throws CannotLoadDocumentException, RemoteInvocationException {
		
		byte[] result;
		
		try {
			result = frontEnd.loadDoc(username, docName);
		} catch (InvalidAttributeValueException | DocDoesNotExist_Exception
				| UserDoesNotExist_Exception e) {
			
			throw new CannotLoadDocumentException(e.getMessage());
		}
		
		return result;
	}
}
