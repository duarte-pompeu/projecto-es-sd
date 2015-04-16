package pt.ulisboa.tecnico.sdis.store.ws;

import java.io.UnsupportedEncodingException;

import javax.xml.ws.Endpoint;

import pt.ulisboa.tecnico.sdis.juddi.UDDINaming;

public class SDStoreMain{
	public static final int STORAGE_CAP = 10 * 1024;
	private static Storage storage;
	// debug mode makes server more verbose, outputting status on calls
	public static final boolean DEBUG_MODE = true;
	
	
	
	public static void main(String[] args){
		
		initStorage();
		populateStorage();
		
		System.out.println("USERS:");
		for(String user: storage.getUsers()){
			System.out.println(user);
		}
		
		// default values
		String uddiURL = "http://localhost:8081";
        String name = "sd-store";
		String url = "http://localhost:8080/store-ws/endpoint";
		
		// replace default values if passed to program
		if(args.length >= 1){
			uddiURL = args[0];
		}
		
		if(args.length >= 2){
			name = args[1];
		}
		
		if(args.length >= 3){
			url = args[2];
		}
		
		Endpoint endpoint = null;
		UDDINaming uddiNaming = null;
		try{
			endpoint = Endpoint.create(new SDStoreImpl(DEBUG_MODE));
			
			// publish endpoint
			System.out.println("Starting " + url);
			endpoint.publish(url);
			
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
	
	
	/**
	 * A simple singleton like method.
	 */
	public static Storage getStorage(){
		if(storage == null){
			initStorage();
		}
		return storage;
	}
	

	public static void initStorage(){
		storage = new Storage(STORAGE_CAP);
	}
	
	
	/**
	 * Populate storage as asked.
	 */
	public static void populateStorage(){
		storage.createCollection("alice");
		storage.createCollection("bruno");
		storage.createCollection("carla");
		storage.createCollection("duarte");
		storage.createCollection("eduardo");
	}
	
	
	/**
	 * Wraps how a string is converted to a byte array.
	 * This leads to simpler code: other classes won't have to try and catch exceptions.
	 * Which is good, because this exception is NEVER actually raised.
	 */
	public static byte[] string2bytes(String s){
		try {
			return s.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	
	/**
	 * Wraps how a byte array is converted to a String.
	 * This leads to simpler code: other classes won't have to try and catch exceptions.
	 * Which is good, because this exception is NEVER actually raised.
	 */
	public static String bytes2string(byte[] bytes){
		try {
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
}
