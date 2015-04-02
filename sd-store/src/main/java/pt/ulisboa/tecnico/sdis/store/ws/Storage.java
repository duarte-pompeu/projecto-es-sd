package pt.ulisboa.tecnico.sdis.store.ws;

import java.util.Collection;
import java.util.TreeMap;

public class Storage {
	private TreeMap<DocUserPair, Doc> treeMap;

	public Storage(){
		init();
	}
	
	private void init(){
		treeMap = new TreeMap<DocUserPair, Doc>();
	}
	
	//FIXME: this should probably be thread-safe, it isn't atm
	// unless treeMap is threadsafe by itself, but dont assume
	public void addDoc(DocUserPair duPair) throws DocAlreadyExists_Exception{
		
		if(treeMap.containsKey(duPair)){
			//FIXME: check if this is how you're supposed to throw
			DocAlreadyExists dae = new DocAlreadyExists();
			dae.setDocId(duPair.getDocumentId());
			throw new DocAlreadyExists_Exception("Doc already exists", dae);
		}
		
		treeMap.put(duPair, null);
	}
	
	public Collection<Doc> getAllDocs(){
		return treeMap.values();
	}
	
	
}
