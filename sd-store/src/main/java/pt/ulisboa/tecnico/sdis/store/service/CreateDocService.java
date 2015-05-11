package pt.ulisboa.tecnico.sdis.store.service;

import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.SDStoreMain;
import pt.ulisboa.tecnico.sdis.store.ws.Storage;
import pt.ulisboa.tecnico.sdis.store.ws.UserRepository;

public class CreateDocService extends SDStoreService{
	String userID;
	String docID;
	
	
	public CreateDocService(String userID, String docID){
		this.userID = userID;
		this.docID = docID;
	}
	
	
	public void dispatch()throws DocAlreadyExists_Exception{
		Storage storage = SDStoreMain.getStorage();
		
		try{
			storage.addDoc(userID, docID);
			
			UserRepository collection = storage.getCollection(userID);
			super.setSeq(collection.getDoc(docID).getVersion());
			super.setUserNumber(collection.getOwnerID());
		}
		
		catch(DocAlreadyExists_Exception daeExcept){
			throw daeExcept;
		}
	}
}
