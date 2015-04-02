package pt.ulisboa.tecnico.sdis.store.ws;

public class Doc {
	private String ownerID;
	private String docID;
	private byte[] contents;
	
	public Doc(String ownerID, String docID){
		this.ownerID = ownerID;
		this.docID = docID;
	}
	
	
	public String getOwnerID(){
		return this.ownerID;
	}
	
	
	public String getDocID(){
		return this.docID;
	}
}
