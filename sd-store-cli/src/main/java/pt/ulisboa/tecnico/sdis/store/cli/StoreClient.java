package pt.ulisboa.tecnico.sdis.store.cli;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import pt.ulisboa.tecnico.sdis.juddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.store.cli.service.CreateDocService;
import pt.ulisboa.tecnico.sdis.store.cli.service.ListDocsService;
import pt.ulisboa.tecnico.sdis.store.cli.service.LoadDocService;
import pt.ulisboa.tecnico.sdis.store.cli.service.StoreDocService;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;

public class StoreClient{
	static SDStore _port;
	
	// default values
	public static String uddiURL = "http://localhost:8081";
	public static String uddiName ="sd-store";
	
	
	public static void main(String[] args) throws Exception{

		if(args.length >= 1){
			uddiURL = args[0];
		}
		
		if(args.length >= 2){
			uddiName = args[1];
		}
		
		
		_port = findUddi(uddiURL, uddiName);
        
	}
	
	
	public static SDStore findUddi(String uddiURL, String uddiName) throws JAXRException {
		SDStore port;
		System.out.printf("Contacting UDDI at %s%n", uddiURL);
        UDDINaming uddiNaming = new UDDINaming(uddiURL);
        
        System.out.printf("Looking for '%s'%n", uddiName);
        String endpointAddress = uddiNaming.lookup(uddiName);
        
        if (endpointAddress == null) {
            System.out.println("Not found!");
            return null;
        } else {
            System.out.printf("Found %s%n", endpointAddress);
        }
        
        System.out.println("Creating stub ...");
        SDStore_Service service = new SDStore_Service();
        port = service.getSDStoreImplPort();
        
        System.out.println("Setting endpoint address ...");
        BindingProvider bindingProvider = (BindingProvider) port;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
        
        return port;
	}


	public static void createDoc(String userID, String docID){
		CreateDocService service = new CreateDocService(userID, docID, _port);
		
		try{
			service.dispatch();
			System.out.printf("Created doc '%d'.\n", docID);
		}
		
		catch (Exception e){
			System.out.println(e.getMessage());
			return;
		}
	}
	
	
	public static void listDocs(String userID){
		ListDocsService service = new ListDocsService(userID, _port);
				
		try{
			service.dispatch();
					
		} catch (Exception e) { 
			System.out.println(e.getMessage());
			return;
		}
		
		System.out.printf("List of user '%s' docs.\n", userID);
		List<String> docs = service.getResult();
		
		for(String s: docs){
			System.out.println(s);
		}
	}
	
	public static void storeDoc(String userID, String docID, String strContent){
		StoreDocService service;
		byte[] content = string2bytes(strContent);
		
		try{
			service = new StoreDocService(userID, docID, content, _port);
			service.dispatch();
			
		} catch (Exception e) { 
			System.out.println(e.getMessage());
			return; 
		}
		
		System.out.printf("Stored %d bytes in doc '%s'.\n", content.length, docID);
	}
	
	public static void loadDoc(String userID, String docID){
		
		byte content[] = null;
		try{
			LoadDocService service = new LoadDocService(userID, docID, _port);
			service.dispatch();
			content = service.getResult();
		} 
		catch (Exception e) { 
			System.out.println(e.getMessage()); 
			return;
		}
		
		System.out.printf("Loaded %d bytes from doc '%s':\n", content.length, docID);
		System.out.println(bytes2string(content));
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
	
	public static SDStore initPort(){
		SDStore_Service service = new SDStore_Service();
		return service.getSDStoreImplPort();
	}
}
