package pt.ulisboa.tecnico.sdis.store.service;

import java.util.List;

import pt.ulisboa.tecnico.sdis.store.ws.SDStoreMain;
import pt.ulisboa.tecnico.sdis.store.ws.Storage;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserRepository;

public class ListDocsService extends SDStoreService{
	private String userID;
	private List<String> result;
	
	
	public ListDocsService(String userID){
		this.userID = userID;
	}
	
	
	public void dispatch() throws UserDoesNotExist_Exception{
		Storage storage = SDStoreMain.getStorage();
		List<String> docsList = storage.getUserDocs(userID);
		
		if(docsList == null){
			UserDoesNotExist udneM = new UserDoesNotExist();
			udneM.setUserId(userID);
			
			throw new UserDoesNotExist_Exception("User does not exist", udneM);
		}
		
		this.result = docsList;
		
		UserRepository collection = storage.getCollection(userID);
		super.setUserNumber(collection.getOwnerID());
		
		super.setSeq(collection.getWriteCount());
	}
	
	
	public List<String> getResult(){
		return this.result;
	}
}