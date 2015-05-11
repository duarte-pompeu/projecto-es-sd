package pt.ulisboa.tecnico.sdis.store.service;

import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.SDStoreMain;
import pt.ulisboa.tecnico.sdis.store.ws.Storage;

public class CreateDocService extends SDStoreService{
	String userID;
	String docID;
	
	
	public CreateDocService(String userID, String docID){
		this(userID, docID, NO_FRONT_END);
	}
	
	
	public CreateDocService(String userID, String docID, String clientID){
		this.userID = userID;
		this.docID = docID;
		this.lastUserWrite = clientID;
	}
	
	
	public void dispatch()throws DocAlreadyExists_Exception{
		Storage storage = SDStoreMain.getStorage();
		
		try{
			storage.addDoc(userID, docID);
			super.touch(docID, userID);
		}
		
		catch(DocAlreadyExists_Exception daeExcept){
			throw daeExcept;
		}
	}
}
