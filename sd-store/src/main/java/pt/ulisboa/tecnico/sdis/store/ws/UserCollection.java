package pt.ulisboa.tecnico.sdis.store.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pt.ulisboa.tecnico.sdis.store.exceptions.StorageCapacityException;

public class UserCollection {
	
	private TreeMap<String, byte[]> docs;
	private String owner;
	private int maxCapacity;
	private int usedCapacity;
	
	public UserCollection(String owner, int capacity){
		this.owner = owner;
		docs = new TreeMap<String, byte[]>();
		usedCapacity = 0;
		maxCapacity = capacity;
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
	
	
	public void setContent(String docID, byte[] newContent) throws StorageCapacityException{
		/* Business rule: we must not exceed the collection capacity size.
		 */
		
		/* Let's start by computing size of old content.
		 */
		byte[] old_content = getContent(docID);
		int old_size = getSize(old_content);
		int new_size = getSize(newContent);
		
		/* Throw exception if used_capacity above max allowed.
		 */
		int new_capacity = this.usedCapacity + new_size - old_size;
		if(new_capacity > maxCapacity){
			throw new StorageCapacityException();
		}
		
		/* If all is good:
		 * 1. update used capacity
		 * 2. update document content
		 */
		usedCapacity += new_size - old_size;
		docs.put(docID, newContent);
	}
	
	
	public List<String> getAllDocs(){
		return new ArrayList<String>(docs.keySet());
	}
	
	
	public int getSize(byte[] content){
		if(content == null){
			return 0;
		}
		
		else return content.length;
	}
}
