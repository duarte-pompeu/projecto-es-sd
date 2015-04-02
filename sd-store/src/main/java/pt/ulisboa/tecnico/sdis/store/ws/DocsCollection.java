package pt.ulisboa.tecnico.sdis.store.ws;

import java.util.ArrayList;
import java.util.List;

public class DocsCollection {
	private ArrayList<Doc> docs;
	private String owner;
	
	
	public DocsCollection(String owner){
		this.owner = owner;
		docs = new ArrayList<Doc>();
	}
	
	
	public String getOwner(){
		return this.owner;
	}
	
	
	public void addDoc(String userID){
		docs.add(new Doc(userID, this.owner));
	}
	
	
	public boolean contains(String docID){
		
		for(Doc d: docs){
			if(d.getDocID().equals(docID)){
				return true;
			}
		}
		
		return false;

	}
	
	public Doc getDoc(String docID){
		for(Doc d: docs){
			if(d.getDocID().equals(docID)){
				return d;
			}
		}
		
		return null;
	}
	
	public List<Doc> getAllDocs(){
		return this.docs;
	}
}
