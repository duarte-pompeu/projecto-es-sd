package pt.ulisboa.tecnico.sdis.store.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;

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
		listDocs("duarte");
		storeDoc("duarte", "tutanova email", "test".getBytes("UTF-8"));
		loadDoc("duarte", "tutanova email");
	}
	
	
	public static void createDoc(String userID, String docID){
		DocUserPair dup = new DocUserPair();
		dup.setUserId(userID);
		dup.setDocumentId(docID);
		
		try{
			_port.createDoc(dup);
		} catch (Exception e) { System.out.println(e.getMessage()); }
	}
	
	
	public static void listDocs(String userID){
		List<String> docs = null;
		
		try{
			docs = _port.listDocs(userID);
		} catch (Exception e) { System.out.println(e.getMessage()); }
		
		System.out.printf("List of user '%s' docs.\n", userID);
		
		for(String s: docs){
			System.out.println(s);
		}
	}
	
	public static void storeDoc(String userID, String docID, byte[] content){
		DocUserPair dup = new DocUserPair();
		dup.setUserId(userID);
		dup.setDocumentId(docID);
		
		try{
			_port.store(dup, content);
		} catch (Exception e) { System.out.println(e.getMessage()); }
	}
	
	public static void loadDoc(String userID, String docID){
		DocUserPair dup = new DocUserPair();
		dup.setUserId(userID);
		dup.setDocumentId(docID);
		
		byte content[] = null;
		try{
			content = _port.load(dup);
		} catch (Exception e) { System.out.println(e.getMessage()); }
		
		try{
			System.out.println(new String(content, "UTF-8"));
		} catch (UnsupportedEncodingException e) { System.out.println(e.getMessage()); }
	}
}