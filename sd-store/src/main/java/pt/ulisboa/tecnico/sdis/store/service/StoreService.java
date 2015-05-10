package pt.ulisboa.tecnico.sdis.store.service;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

import javax.crypto.SecretKey;

import pt.tecnico.sd.SdCrypto;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.SDStoreMain;
import pt.ulisboa.tecnico.sdis.store.ws.Storage;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserRepository;

public class StoreService {
	String userID;
	String docID;
	byte[] content;
	
	public StoreService(String userID, String docID, byte[] content){
		this.userID = userID;
		this.docID = docID;
		this.content = content;
	}
	
	public void dispatch() throws UserDoesNotExist_Exception, CapacityExceeded_Exception, DocDoesNotExist_Exception{
		boolean MACisValid = checkMAC(SDStoreMain.RECEIVED_MAC_STR);
		
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
		} catch (CapacityExceeded_Exception e){ throw e; }
	}
	
	public boolean checkMAC(String macString){
    	byte[] bytes4mac = SDStoreMain.string2bytes("TESTEMAC");
		SecretKey macKey = SdCrypto.generateMacKey(bytes4mac);
		byte[] mac =  parseBase64Binary(macString);
		
		System.out.println("\n\n ############################################ \n\n");
		
		boolean result = SdCrypto.verifyMac(content, mac, macKey);
		if(result){
			System.out.println("MAC: YES!!!");
		}
		else{
			System.out.println("MAC: NO :(");
		}
		
		System.out.println("\n\n ############################################ \n\n");
		
		return result;
    }
}
