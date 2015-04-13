package pt.ulisboa.tecnico.sdis.store.cli.service;

import javax.naming.directory.InvalidAttributeValueException;

import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;

public class CreateDocService {
	String userID;
	String docID;
	SDStore port;
	
	public CreateDocService(String userID, String docID, SDStore port){
		this.userID = userID;
		this.docID = docID;
		this.port = port;
	}
	
	public void dispatch() throws DocAlreadyExists_Exception, InvalidAttributeValueException{
		
		if(port == null ){
			throw new InvalidAttributeValueException("Port is null");
		}
		
		DocUserPair dup = new DocUserPair();
		dup.setUserId(userID);
		dup.setDocumentId(docID);
		
		port.createDoc(dup);
	}
}
