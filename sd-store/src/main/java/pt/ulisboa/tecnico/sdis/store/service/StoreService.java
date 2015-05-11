package pt.ulisboa.tecnico.sdis.store.service;

import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.SDStoreMain;
import pt.ulisboa.tecnico.sdis.store.ws.Storage;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserRepository;

public class StoreService extends SDStoreService{
	String userID;
	String docID;
	byte[] content;
	
	public StoreService(String userID, String docID, byte[] content){
		this(userID, docID, content, NO_FRONT_END);
	}
	
	public StoreService(String userID, String docID, byte[] content, String clientID){
		this.userID = userID;
		this.docID = docID;
		this.content = content;
		this.lastUserWrite = clientID;
	}
	
	
	public void dispatch() throws UserDoesNotExist_Exception, CapacityExceeded_Exception, DocDoesNotExist_Exception{
		boolean MACisValid = checkMAC(SDStoreMain.RECEIVED_MAC_STR, this.content);
		
		if( !MACisValid){
			//FIXME do not throw an exception that doesnt make any sense
			
			UserDoesNotExist udneM = new UserDoesNotExist();
			udneM.setUserId(userID);
			
			throw new UserDoesNotExist_Exception("BAD MAC", udneM);
		}
		SDStoreMain.RECEIVED_MAC_STR = null;
		
		
		Storage storage = SDStoreMain.getStorage();
		
		UserRepository collection = storage.getCollection(userID);
		
		if(collection == null){
			UserDoesNotExist udneM = new UserDoesNotExist();
			udneM.setUserId(userID);
			
			throw new UserDoesNotExist_Exception("User does not exist", udneM);
		}
		
		if(!collection.contains(docID)){
			DocDoesNotExist fault = new DocDoesNotExist();
			fault.setDocId(docID);
			throw new DocDoesNotExist_Exception("Doc does not exist!", fault);
		}
		
		try{
			collection.setContent(docID, content);
			super.touch(docID, userID);
			
		} catch (CapacityExceeded_Exception e){ throw e; }
	}
}
