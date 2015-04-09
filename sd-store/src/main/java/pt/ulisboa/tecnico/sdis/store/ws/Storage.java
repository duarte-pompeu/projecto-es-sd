package pt.ulisboa.tecnico.sdis.store.ws;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class Storage {
	private TreeMap<String, UserCollection> collections;

	public Storage(){
		init();
	}
	
	
	public void init(){
		collections = new TreeMap<String, UserCollection>();
	}
	
	
	public UserCollection getCollection(String userID){
		return collections.get(userID);
	}
	
	
	public List<String> getUserDocs(String userID)
		throws UserDoesNotExist_Exception{
		
		UserCollection collection = this.getCollection(userID);
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
		Collection<UserCollection> collections_set = collections.values();
		
		ArrayList<String> docs = new ArrayList<String>();
		
		for(UserCollection col: collections_set){
			docs.addAll(col.getAllDocs());
		}
		
		return docs;
	}
	
	public UserCollection createCollection(String userID){
		UserCollection col = new UserCollection(userID);
		this.collections.put(userID, col);
		
		return col;
	}
	
	
	//FIXME: this should probably be thread-safe, it may not be atm
	public void addDoc(String user, String doc) throws DocAlreadyExists_Exception{
		
		/* Check if user has collection and get it.
		 * Create a new one otherwise.
		 */
		UserCollection collection = getCollection(user);
		
		if(collection == null){
			collection = this.createCollection(user);
			
		}
		
		/* Check if doc exists - if it does, throw exception.
		 */
		if(collection.contains(doc)){
			//FIXME: check if this is how you're supposed to throw
			DocAlreadyExists dae = new DocAlreadyExists();
			dae.setDocId(doc);
			throw new DocAlreadyExists_Exception("Doc already exists", dae);
		}
		
		collection.addDoc(doc);
	}
}
