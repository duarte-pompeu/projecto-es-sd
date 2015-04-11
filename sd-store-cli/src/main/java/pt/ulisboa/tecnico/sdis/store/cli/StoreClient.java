package pt.ulisboa.tecnico.sdis.store.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.store.cli.service.CreateDocService;
import pt.ulisboa.tecnico.sdis.store.cli.service.ListDocsService;
import pt.ulisboa.tecnico.sdis.store.cli.service.LoadDocService;
import pt.ulisboa.tecnico.sdis.store.cli.service.StoreDocService;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;

public class StoreClient{
	static SDStore _port;
	
	public static void main(String[] args) throws Exception{
		SDStore_Service service = new SDStore_Service();
		_port = service.getSDStoreImplPort();
		
		BindingProvider bindingProvider = (BindingProvider) _port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		
		System.out.println("Endpoint address:");
		System.out.println(requestContext.get(ENDPOINT_ADDRESS_PROPERTY));
		
		createDoc("duarte", "tutanota email");
		storeDoc("duarte", "tutanota email", "teste");
		loadDoc("duarte", "tutanota email");
		listDocs("duarte");
	}
	
	
	public static void createDoc(String userID, String docID){
		CreateDocService service = new CreateDocService(userID, docID, _port);
		
		try{
			service.dispatch();
			System.out.printf("Created doc '%d'.\n", docID);
		}
		
		catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	
	public static void listDocs(String userID){
		ListDocsService service = new ListDocsService(userID, _port);
				
		try{
			service.dispatch();
					
		} catch (Exception e) { System.out.println(e.getMessage()); }
		
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
			
		} catch (Exception e) { System.out.println(e.getMessage()); }
		
		System.out.printf("Stored %d bytes in doc '%s'.\n", content.length, docID);
	}
	
	public static void loadDoc(String userID, String docID){
		
		byte content[] = null;
		try{
			LoadDocService service = new LoadDocService(userID, docID, _port);
			service.dispatch();
			content = service.getResult();
			
		} catch (Exception e) { System.out.println(e.getMessage()); }
		
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
}
