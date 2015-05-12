package pt.ulisboa.tecnico.sdis.store.service;

import java.io.UnsupportedEncodingException;

import pt.ulisboa.tecnico.sdis.store.ws.Document;
import pt.ulisboa.tecnico.sdis.store.ws.SDStoreMain;
import pt.ulisboa.tecnico.sdis.store.ws.Storage;
import pt.ulisboa.tecnico.sdis.store.ws.UserRepository;

public class SDStoreService{
	private int seq = -1;
	private static int lastSeq = -1;
	
	private int userNumber = -1;
	private static int lastUserNumber = -1;
	
	protected String lastUserWrite;
	
	protected static final String NO_FRONT_END = "-1";
	
	public void touch(String docID, String userID){
		Storage storage = SDStoreMain.getStorage();
		
		UserRepository repo = storage.getCollection(userID);
		repo.setLastUserWrite(lastUserWrite);
		
		Document doc = repo.getDoc(docID);
		doc.setLastUserWrite(lastUserWrite);
		
		setSeq(doc.getVersion());
		setUserNumber(Integer.valueOf(lastUserWrite));
		setLastUser(doc.getLastUserWrite());
	}
	
	public void inspectDocTouch(String docID, String userID){
		Storage storage = SDStoreMain.getStorage();
		
		UserRepository repo = storage.getCollection(userID);
		Document doc = repo.getDoc(docID);
		setSeq(doc.getVersion());
		setUserNumber(Integer.valueOf(doc.getLastUserWrite()));
		setLastUser(doc.getLastUserWrite());
	}
	
	public void inspectRepoTouch(String userID){
		Storage storage = SDStoreMain.getStorage();
		
		UserRepository repo = storage.getCollection(userID);
		setSeq(repo.getWriteCount());
		setUserNumber(Integer.valueOf(repo.getLastUserWrite()));
		setLastUser(repo.getLastUserWrite());
	}
	
	/*
	public boolean checkMAC(String macString, byte[] content){
		if(macString == null){
			return true;
		}
		
    	byte[] bytes4mac = string2bytes("TESTEMAC");
		SecretKey macKey = SdCrypto.generateMacKey(bytes4mac);
		byte[] mac =  parseBase64Binary(macString);
		
		System.out.println("\n\n############################################");
		
		boolean result = SdCrypto.verifyMac(content, mac, macKey);
		if(result){
			System.out.println("MAC: YES!!!");
		}
		else{
			System.out.println("MAC: NO :(");
		}
		
		System.out.println("############################################\n\n");
		
		return result;
    }
    */
	
	/**
	 * Wraps how a string is converted to a byte array.
	 * This leads to simpler code: other classes won't have to try and catch exceptions.
	 * Which is good, because this exception is NEVER actually raised.
	 */
	public static byte[] string2bytes(String s){
		try {
			return s.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	
	/**
	 * Wraps how a byte array is converted to a String.
	 * This leads to simpler code: other classes won't have to try and catch exceptions.
	 * Which is good, because this exception is NEVER actually raised.
	 */
	public static String bytes2string(byte[] bytes){
		try {
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	
	public void setSeq(int seq){
		this.seq = seq;
		lastSeq = seq;
	}
	
	public void setUserNumber(int userNumber){
		this.userNumber = userNumber;
		lastUserNumber = userNumber;
	}
	
	public static int lastSeq(){
		return lastSeq;
	}
	
	public static int lastUserNumber(){
		return lastUserNumber;
	}
	
	
	public int getSeq(){
		return this.seq;
	}
	
	public int getUserNumber(){
		return this.userNumber;
	}

	public void setLastUser(String clientID) {
		this.lastUserWrite = clientID;
	}
}
