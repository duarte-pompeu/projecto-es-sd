package pt.ulisboa.tecnico.sdis.store.cli.service;

import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class StoreDocService {
	String userID;
	String docID;
	SDStore port;
	byte[] content;

	public StoreDocService(String userID, String docID, byte[] content, SDStore port) {
		this.userID = userID;
		this.docID = docID;
		this.content = content;
		this.port = port;
	}
	
	public void dispatch() throws CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception{
		DocUserPair dup = new DocUserPair();
		dup.setUserId(userID);
		dup.setDocumentId(docID);
		
		port.store(dup, content);
	}
}
