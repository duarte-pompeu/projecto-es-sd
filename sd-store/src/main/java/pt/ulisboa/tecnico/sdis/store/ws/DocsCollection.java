package pt.ulisboa.tecnico.sdis.store.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class DocsCollection {
	private TreeMap<String, byte[]> docs;
	private String owner;
	
	
	public DocsCollection(String owner){
		this.owner = owner;
		docs = new TreeMap<String, byte[]>();
	}
	
	
	public String getOwner(){
		return this.owner;
	}
	
	
	public void addDoc(String docID){
		docs.put(docID, null);
	}
	
	
	public boolean contains(String docID){
		return docs.containsValue(docID);
	}
	
	
	public byte[] getContent(String docID){
		return docs.get(docID);
	}
	
	public List<String> getAllDocs(){
		return new ArrayList<String>(docs.keySet());
	}
}
