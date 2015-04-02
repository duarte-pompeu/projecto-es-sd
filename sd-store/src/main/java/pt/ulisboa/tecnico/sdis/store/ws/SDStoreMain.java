package pt.ulisboa.tecnico.sdis.store.ws;

public class SDStoreMain{
	private static Storage db;
	
	public static void main(String[] args){
		initDB();
		System.out.println("Don't worry, at least it compiles.");
	}
	
	
	public static Storage getDB(){
		if(db == null){
			initDB();
		}
		return db;
	}
	
	
	public static void initDB(){
		db = new Storage();
	}
}
