package pt.ulisboa.tecnico.sdis.store.service;

public class LoadService {
	String userID;
	String docID;
	private byte[] result;
	
	public LoadService(String userID, String docID){
		this.userID = userID;
		this.docID = docID;
	}
	
	public void dispatch(){
		
		result = null;
	}
	
	public byte[] getResult(){
		
		return this.result;
	}
}
