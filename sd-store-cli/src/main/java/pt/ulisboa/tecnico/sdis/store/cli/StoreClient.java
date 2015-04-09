package pt.ulisboa.tecnico.sdis.store.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Map;

import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;

public class StoreClient{
	public static void main(String[] args) throws Exception{
		SDStore_Service service = new SDStore_Service();
		SDStore port = service.getSDStoreImplPort();
		
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		
		System.out.println("Endpoint address:");
		System.out.println(requestContext.get(ENDPOINT_ADDRESS_PROPERTY));
		
		DocUserPair dup = new DocUserPair();
		dup.setUserId("jubi");
		dup.setDocumentId("tutanota mail");
		
		port.createDoc(dup);
		
		for(String s: port.listDocs(dup.getUserId())){
			System.out.println(s);
		}
		
	}
}