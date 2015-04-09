package pt.ulisboa.tecnico.sdis.store.ws;

import javax.xml.ws.Endpoint;

public class SDStoreMain{
	public static final int STORAGE_CAP = 10 * 1024;
	private static Storage storage;
	
	public static void main(String[] args){
		boolean debug_mode = true;
		
		initStorage();
		populateStorage();
		
		System.out.println("USERS:");
		for(String user: storage.getUsers()){
			System.out.println(user);
		}
		
		String url = "http://localhost:8080/store-ws/endpoint";
		Endpoint endpoint = null;
		
		try{
			endpoint = Endpoint.create(new SDStoreImpl(debug_mode));
			
			// publish endpoint
			System.out.println("Starting " + url);
			endpoint.publish(url);
			
			// wait
			System.out.println("Awaiting connections");
            System.out.println("Press enter to shutdown");
            System.in.read();
		}
		
		catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		finally{
			try{
				if(endpoint != null){
					endpoint.stop();
					System.out.println("Stopped endpoint " + url);
				}
			} catch (Exception e){
				System.out.println(e);
			}
		}
	}
	
	
	public static Storage getStorage(){
		if(storage == null){
			initStorage();
		}
		return storage;
	}
	
	
	public static void initStorage(){
		storage = new Storage(STORAGE_CAP);
	}
	
	public static void populateStorage(){
		storage.createCollection("alice");
		storage.createCollection("bruno");
		storage.createCollection("carla");
		storage.createCollection("duarte");
		storage.createCollection("eduardo");
	}
}
