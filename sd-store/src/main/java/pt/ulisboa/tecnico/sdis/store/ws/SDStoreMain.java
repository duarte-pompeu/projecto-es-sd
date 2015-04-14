package pt.ulisboa.tecnico.sdis.store.ws;

import java.io.UnsupportedEncodingException;

import javax.xml.ws.Endpoint;

import pt.ulisboa.tecnico.sdis.juddi.UDDINaming;

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
		String uddiURL = "http://localhost:8081";
        String name = "SDStore";
		String url = "http://localhost:8080/store-ws/endpoint";
		
		Endpoint endpoint = null;
		UDDINaming uddiNaming = null;
		try{
			endpoint = Endpoint.create(new SDStoreImpl(debug_mode));
			
			// publish endpoint (no uddi)
//			System.out.println("Starting " + url);
//			endpoint.publish(url);
			
			// publish to UDDI
            System.out.printf("Publishing '%s' to UDDI at %s%n", name, uddiURL);
            uddiNaming = new UDDINaming(uddiURL);
            uddiNaming.rebind(name, url);
			
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
			
			try {
                if (uddiNaming != null) {
                    // delete from UDDI
                    uddiNaming.unbind(name);
                    System.out.printf("Deleted '%s' from UDDI%n", name);
                }
            } catch(Exception e) {
                System.out.printf("Caught exception when deleting: %s%n", e);
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
	
	
	public static byte[] string2bytes(String s){
		try {
			return s.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	
	public static String bytes2string(byte[] bytes){
		try {
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
}
