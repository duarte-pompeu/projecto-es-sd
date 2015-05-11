package pt.ulisboa.tecnico.sdis.store.service;

import pt.ulisboa.tecnico.sdis.store.ws.Document;
import pt.ulisboa.tecnico.sdis.store.ws.SDStoreMain;
import pt.ulisboa.tecnico.sdis.store.ws.Storage;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserRepository;

public class LoadService extends SDStoreService{
	String userID;
	String docID;
	private byte[] result;
	private int version;
	
	
	public LoadService(String userID, String docID){
		this.userID = userID;
		this.docID = docID;
	}
	
	
	public void dispatch() throws UserDoesNotExist_Exception {
		Storage storage = SDStoreMain.getStorage();
		
		UserRepository collection = storage.getCollection(userID);
		
		if(collection == null){
			UserDoesNotExist udneM = new UserDoesNotExist();
			udneM.setUserId(userID);
			
			throw new UserDoesNotExist_Exception("User does not exist", udneM);
		}
		
		if(!collection.contains(docID)){
			//doc does not exist
		}
		
		Document doc = collection.getDoc(docID);
		result = doc.getContent();
		version = doc.getVersion();
	}
	
	public byte[] getResult(){
		return this.result;
	}

	
	public String getTag() {
		//TODO
		return null;
	}
}
