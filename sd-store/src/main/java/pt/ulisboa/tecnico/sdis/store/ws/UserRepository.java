package pt.ulisboa.tecnico.sdis.store.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class UserRepository {
	
	private TreeMap<String, Document> docs;
	private String owner;
	private int maxCapacity;
	private int usedCapacity;
	
	
	
	public UserRepository(String owner, int capacity){
		this.owner = owner;
		docs = new TreeMap<String, Document>();
		usedCapacity = 0;
		maxCapacity = capacity;
	}
	
	
	public String getOwner(){
		return this.owner;
	}
	
	
	public void addDoc(String docID){
		docs.put(docID, new Document(docID));
	}
	
	
	public boolean contains(String docID){
		return docs.containsKey(docID);
	}
	
	
	public byte[] getContent(String docID){
		return docs.get(docID).getContent();
	}
	
	
	public void setContent(String docID, byte[] newContent) throws CapacityExceeded_Exception{
		// Business rule: we must not exceed the collection capacity size.
		
		// Let's start by computing size of old content.
		byte[] old_content = getContent(docID);
		int old_size = getSize(old_content);
		int new_size = getSize(newContent);
		
		/* Throw exception if used_capacity above max allowed.
		 * Reminder: capacity refers to the whole collection, not to a single doc.
		 */
		int new_capacity = this.usedCapacity + new_size - old_size;
		if(new_capacity > maxCapacity){
			CapacityExceeded cap = new CapacityExceeded();
			cap.setCurrentSize(new_capacity);
			cap.setAllowedCapacity(maxCapacity);
			
			String except_msg = "You tried to store " + new_capacity + " bytes. ";
			except_msg += "Max capacity allowed is "+ maxCapacity +" bytes . ";
			throw new CapacityExceeded_Exception(except_msg, cap);
		}
		
		// if we haven't returned or thrown an exception, it means all is good and the new content should be stored.
		// reminder: update the used capacity
		usedCapacity += new_size - old_size;
		docs.put(docID, new Document(docID, newContent));
	}
	
	
	public List<String> getAllDocs(){
		return new ArrayList<String>(docs.keySet());
	}
	
	
	/**
	 * This method allows checking an array size without worrying if its null or not.
	 * If it's null? Return 0.
	 */
	public int getSize(byte[] content){
		if(content == null){
			return 0;
		}
		
		else return content.length;
	}


	public Document getDoc(String docID) {
		return docs.get(docID);
	}
}
