package pt.ulisboa.tecnico.sdis.store.ws;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.registry.JAXRException;
import javax.xml.ws.Endpoint;

import pt.ulisboa.tecnico.sdis.juddi.UDDINaming;

public class SDStoreMain{
	public static final int STORAGE_CAP = 10 * 1024;
	private static Storage storage;
	// debug mode makes server more verbose, outputting status on calls
	public static final boolean DEBUG_MODE = true;
	public static UDDINaming UDDI_NAMING;
	
	
	
	public static void main(String[] args){
		
		initStorage();
		populateStorage();
		
		System.out.println("USERS:");
		for(String user: storage.getUsers()){
			System.out.println(user);
		}
		
		// default values
		String uddiURL = "http://localhost:8081";
        String name = "SD-STORE";
		
		// replace default values if passed to program
		if(args.length >= 1){
			uddiURL = args[0];
		}
		
		UDDINaming listUddiNames;
		try {
			listUddiNames = new UDDINaming(uddiURL);
		} catch (JAXRException e1) {
			e1.printStackTrace();
			System.out.println((e1));
			System.out.println("ERROR: cant create UDDINaming.");
			return;
		}
		
		if(args.length >= 2){
			name = args[1];
		}
		
		ArrayList<String> endPoints = new ArrayList<String>();
		
		
		if(args.length >= 5){
			String host = args[2];
			int port = Integer.valueOf(args[3]);
			String path = args[4];
			int nReplicas = Integer.valueOf(args[5]);
					
			for(int i = 0; i < nReplicas; i++){
				
				String newEndpoint = new String( host + (port-i) + path);
				
				endPoints.add(newEndpoint);
			}
		}
		
		Collection<String> uddiNames = new ArrayList<String>();
		try {
			uddiNames = listUddiNames.list(name + "-%");
		} catch (JAXRException e1) {
			e1.printStackTrace();
			System.out.println((e1));
			System.out.println("ERROR: cant list UDDI names");
		}
		
		// cant publish? endpoint address already taken? no worries, here's a pack of alternative endpoint addresses for 9.99$ only.
		for (int i = 0; i < endPoints.size(); i++){
			String altEP = endPoints.get(i);
			
			try{
				String replica_name = name + "-" + (i+1);
				
				if(uddiNames.contains(altEP)){
					continue;
				}
				
				publish(altEP, uddiURL,replica_name);
				break;
			}
			catch(com.sun.xml.ws.server.ServerRtException e){
				continue;
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		if(UDDI_NAMING == null){
			System.out.println("UDDI error: either (1) multiple servers are publishing to UDDI at the same time"
					+ " or (2) you've exhausted your number of allowed replicas (see sd-store pom.xml <ws.nReplicas>)");
		}
	}
	
	
	public static void publish(String endpointURL, String uddiURL, String uddiName) throws Exception{
		Endpoint endpoint = null;
		try{
			endpoint = Endpoint.create(new SDStoreImpl(DEBUG_MODE));
			
			// publish endpoint
			System.out.println("Starting " + endpointURL);
			endpoint.publish(endpointURL);
			
			// publish to UDDI
            System.out.printf("Publishing '%s' to UDDI at %s%n", uddiName, uddiURL);
            UDDI_NAMING = new UDDINaming(uddiURL);
            UDDI_NAMING.rebind(uddiName, endpointURL);
			
           
			// wait
            System.out.println("FINAL ENDPOINT: " + endpointURL);
			System.out.println("Awaiting connections");
            System.out.println("Press enter to shutdown");
            System.in.read();
		}
		
		catch(Exception e){
			throw e;
		}
		
		finally{
			try{
				if(endpoint != null){
					endpoint.stop();
					System.out.println("Stopped endpoint " + uddiURL);
				}
			} catch (Exception e){
				System.out.println(e);
			}
			
			try {
                if (UDDI_NAMING != null) {
                    // delete from UDDI
                	UDDI_NAMING.unbind(uddiName);
                    System.out.printf("Deleted '%s' from UDDI%n", uddiName);
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
		
		try {
			
			storage.addDoc("alice", "a1");
			storage.addDoc("alice", "a2");
			storage.getCollection("alice").getDoc("a1").setContent(string2bytes("AAAAAAAAAA"));
			storage.getCollection("alice").getDoc("a2").setContent(string2bytes("aaaaaaaaaa"));
			
			storage.addDoc("bruno", "b1");
			storage.getCollection("bruno").getDoc("b1").setContent(string2bytes("BBBBBBBBBBBBBBBBBBBB"));
			
		} catch (DocAlreadyExists_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
