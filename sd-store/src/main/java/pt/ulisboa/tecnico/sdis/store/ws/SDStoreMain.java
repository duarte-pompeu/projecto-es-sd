package pt.ulisboa.tecnico.sdis.store.ws;

public class SDStoreMain{
	private static Storage storage;
	
	public static void main(String[] args){
		initStorage();
		System.out.println("Don't worry, at least it compiles.");
	}
	
	
	public static Storage getStorage(){
		if(storage == null){
			initStorage();
		}
		return storage;
	}
	
	
	public static void initStorage(){
		storage = new Storage();
	}
}
