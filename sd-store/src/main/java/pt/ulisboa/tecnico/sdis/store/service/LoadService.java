package pt.ulisboa.tecnico.sdis.store.service;

import pt.ulisboa.tecnico.sdis.store.ws.SDStoreMain;
import pt.ulisboa.tecnico.sdis.store.ws.Storage;
import pt.ulisboa.tecnico.sdis.store.ws.UserCollection;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class LoadService {
	String userID;
	String docID;
	private byte[] result;
	
	public LoadService(String userID, String docID){
		this.userID = userID;
		this.docID = docID;
	}
	
	public void dispatch() throws UserDoesNotExist_Exception {
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
		
		result = collection.getContent(docID);
	}
	
	public byte[] getResult(){
		
		return this.result;
	}
}
