package pt.ulisboa.tecnico.sdis.store.cli.service;

import javax.naming.directory.InvalidAttributeValueException;

import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class StoreDocService extends ClientService{
	String userID;
	String docID;
	byte[] content;

	public StoreDocService(String userID, String docID, byte[] content, SDStore port) {
		super(port);
		this.userID = userID;
		this.docID = docID;
		this.content = content;
		
		super.addMacDigest(this.content);
	}
	
	public void dispatch() throws CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception, InvalidAttributeValueException{
		
		if(port == null ){
			throw new InvalidAttributeValueException("Port is null");
		}
		
		DocUserPair dup = new DocUserPair();
		dup.setUserId(userID);
		dup.setDocumentId(docID);
		
		try {
			port.store(dup, content);
		} catch (CapacityExceeded_Exception e) {
			throw e;
		} catch (DocDoesNotExist_Exception e) {
			throw e;
		} catch (UserDoesNotExist_Exception e) {
			throw e;
		}
		finally{
			super.afterDispatch();
		}
	}
}
