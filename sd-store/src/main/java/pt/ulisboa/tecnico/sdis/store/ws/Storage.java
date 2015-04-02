package pt.ulisboa.tecnico.sdis.store.ws;

import java.util.List;
import java.util.TreeMap;

public class Storage {
	private TreeMap<String, DocsCollection> collections;

	public Storage(){
		init();
	}
	
	
	private void init(){
		collections = new TreeMap<String, DocsCollection>();
	}
	
	
	public DocsCollection getCollection(String userID){
		return collections.get(userID);
	}
	
	
	public List getUserDocs(String userID){
		return getCollection(userID).getAllDocs();
	}
	
	
	//FIXME: this should probably be thread-safe, it may not be atm
	public void addDoc(DocUserPair duPair) 
			throws DocAlreadyExists_Exception, UserDoesNotExist_Exception{
		String user = duPair.getUserId();
		String doc = duPair.getDocumentId();
		
		
		/* Check if user exists - it should
		 */
		DocsCollection col = getCollection(user);
		
		if(col == null){
			//FIXME: check if this is how you're supposed to throw
			UserDoesNotExist udne = new UserDoesNotExist();
			udne.setUserId(user);
			throw new UserDoesNotExist_Exception("User " + user + " does not exist", udne);
		}
		
		/* Check if doc exists - it should not
		 */
		if(col.contains(doc)){
			//FIXME: check if this is how you're supposed to throw
			DocAlreadyExists dae = new DocAlreadyExists();
			dae.setDocId(duPair.getDocumentId());
			throw new DocAlreadyExists_Exception("Doc already exists", dae);
		}
		
		col.addDoc(user);
	}
	

}
