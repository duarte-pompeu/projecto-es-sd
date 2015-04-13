package pt.ulisboa.tecnico.sdis.store.cli.service;

import javax.naming.directory.InvalidAttributeValueException;

import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class LoadDocService {
	String userID;
	String docID;
	SDStore port;
	byte[] result;
	
	
	public LoadDocService(String userID, String docID, SDStore port) {
		this.userID = userID;
		this.docID = docID;
		this.port = port;
	}
	
	public void dispatch() throws DocDoesNotExist_Exception, UserDoesNotExist_Exception, InvalidAttributeValueException{
		
		if(port == null ){
			throw new InvalidAttributeValueException("Port is null");
		}
		
		DocUserPair dup = new DocUserPair();
		dup.setUserId(userID);
		dup.setDocumentId(docID);
		
		this.result = port.load(dup);
	}

	public byte[] getResult() {
		return result;
	}
	
}
