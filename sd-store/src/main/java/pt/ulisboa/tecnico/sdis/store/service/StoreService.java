package pt.ulisboa.tecnico.sdis.store.service;

import pt.ulisboa.tecnico.sdis.store.ws.SDStoreMain;
import pt.ulisboa.tecnico.sdis.store.ws.Storage;

public class StoreService {
	String userID;
	String docID;
	byte[] content;
	
	public StoreService(String userID, String docID, byte[] content){
		this.userID = userID;
		this.docID = docID;
		this.content = content;
	}
	
	public void dispatch(){
		Storage storage = SDStoreMain.getStorage();
	}
}
