package pt.ulisboa.tecnico.sdis.store.service;

import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.SDStoreMain;
import pt.ulisboa.tecnico.sdis.store.ws.Storage;
import pt.ulisboa.tecnico.sdis.store.ws.UserCollection;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class StoreService {
	String userID;
	String docID;
	byte[] content;
	
	public StoreService(String userID, String docID, byte[] content){
		this.userID = userID;
		this.docID = docID;
		this.content = content;
	}
	
	public void dispatch() throws UserDoesNotExist_Exception, CapacityExceeded_Exception{
		Storage storage = SDStoreMain.getStorage();
		
		UserCollection collection = storage.getCollection(userID);
		
		if(collection == null){
			UserDoesNotExist udneM = new UserDoesNotExist();
			udneM.setUserId(userID);
			
			throw new UserDoesNotExist_Exception("User does not exist", udneM);
		}
		
		if(!collection.contains(docID)){
			//doc does not exist
		}
		
		try{
			collection.setContent(docID, content);
		} catch (CapacityExceeded_Exception e){ throw e; }
	}
}
