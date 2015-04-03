package pt.ulisboa.tecnico.sdis.store.ws;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class Storage {
	private TreeMap<String, DocsCollection> collections;

	public Storage(){
		init();
	}
	
	
	public void init(){
		collections = new TreeMap<String, DocsCollection>();
	}
	
	
	public DocsCollection getCollection(String userID){
		return collections.get(userID);
	}
	
	
	public List<String> getUserDocs(String userID){
		return getCollection(userID).getAllDocs();
	}
	
	
	public List<String> getUsers(){
		Set<String> key_set = collections.keySet();

		return new ArrayList<String>(key_set);
	}
	
	public List<String> getAllDocs(){
		Collection<DocsCollection> collections_set = collections.values();
		
		ArrayList<String> docs = new ArrayList<String>();
		
		for(DocsCollection col: collections_set){
			docs.addAll(col.getAllDocs());
		}
		
		return docs;
	}
	
	public void addCollection(String userID, DocsCollection docsCol){
		this.collections.put(userID, docsCol);
	}
	
	
	//FIXME: this should probably be thread-safe, it may not be atm
	public void addDoc(String user, String doc) 
			throws DocAlreadyExists_Exception{
		
		/* Check if user exists - it should
		 */
		DocsCollection col = getCollection(user);
		
		if(col == null){
			col = new DocsCollection(user);
			this.addCollection(user, col);
		}
		
		/* Check if doc exists - it should not
		 */
		if(col.contains(doc)){
			//FIXME: check if this is how you're supposed to throw
			DocAlreadyExists dae = new DocAlreadyExists();
			dae.setDocId(doc);
			throw new DocAlreadyExists_Exception("Doc already exists", dae);
		}
		
		col.addDoc(doc);
	}
}
