package pt.ulisboa.tecnico.sdis.store.ws;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class Storage {
	private final int STORAGE_CAP;
	private TreeMap<String, UserRepository> collections;

	
	public Storage(int capacity){
		init();
		STORAGE_CAP = capacity;
	}
	
	
	public void init(){
		collections = new TreeMap<String, UserRepository>();
	}
	
	
	public UserRepository getCollection(String userID){
		return collections.get(userID);
	}
	
	
	public List<String> getUserDocs(String userID)
		throws UserDoesNotExist_Exception{
		
		UserRepository collection = this.getCollection(userID);
		List<String> docs = null;
		
		if(collection != null){
			docs = collection.getAllDocs();
		}
		
		if(collection == null || docs == null){
			String message = "User " + userID + " has no docs.";
			UserDoesNotExist faultInfo = new UserDoesNotExist();
			throw new UserDoesNotExist_Exception(message, faultInfo);
		}
		
		return docs;
	}
	
	
	public List<String> getUsers(){
		Set<String> key_set = collections.keySet();

		return new ArrayList<String>(key_set);
	}
	
	
	public List<String> getAllDocs(){
		Collection<UserRepository> collections_set = collections.values();
		
		ArrayList<String> docs = new ArrayList<String>();
		
		for(UserRepository col: collections_set){
			docs.addAll(col.getAllDocs());
		}
		
		return docs;
	}
	
	
	public UserRepository createCollection(String userID){
		UserRepository col = new UserRepository(userID, STORAGE_CAP);
		this.collections.put(userID, col);
		
		return col;
	}
	
	
	//FIXME: should this be thread safe?
	public void addDoc(String user, String doc) throws DocAlreadyExists_Exception{
		
		/* Check if user has collection and get it.
		 * Create a new one otherwise.
		 */
		UserRepository collection = getCollection(user);
		
		if(collection == null){
			collection = this.createCollection(user);
		}
		
		/* Check if doc exists - if it does, throw exception.
		 */
		if(collection.contains(doc)){
			DocAlreadyExists dae = new DocAlreadyExists();
			dae.setDocId(doc);
			throw new DocAlreadyExists_Exception("Doc already exists", dae);
		}
		
		collection.addDoc(doc);
	}

	
	public boolean hasUser(String userID) {
		for(String owner: this.collections.keySet()){
			if(owner.equals(userID)){
				return true;
			}
		}
		return false;
	}
}
