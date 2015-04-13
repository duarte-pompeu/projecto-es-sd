package pt.ulisboa.tecnico.sdis.store.service;

import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.SDStoreMain;
import pt.ulisboa.tecnico.sdis.store.ws.Storage;

public class CreateDocService {
	String userID;
	String docID;
	
	public CreateDocService(String userID, String docID){
		this.userID = userID;
		this.docID = docID;
	}
	
	
	public void dispatch()throws DocAlreadyExists_Exception{
		
		Storage storage = SDStoreMain.getStorage();
		
		if(storage.hasUser(userID)){
			try{
				storage.addDoc(userID, docID);
			}
			
			catch(DocAlreadyExists_Exception daeExcept){
				throw daeExcept;
			}
		}
	}
}
