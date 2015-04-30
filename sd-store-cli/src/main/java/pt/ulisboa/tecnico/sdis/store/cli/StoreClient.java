package pt.ulisboa.tecnico.sdis.store.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.naming.directory.InvalidAttributeValueException;
import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.juddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.store.cli.service.CreateDocService;
import pt.ulisboa.tecnico.sdis.store.cli.service.ListDocsService;
import pt.ulisboa.tecnico.sdis.store.cli.service.LoadDocService;
import pt.ulisboa.tecnico.sdis.store.cli.service.StoreDocService;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class StoreClient{	
	// default values
	public static final String DEFAULT_UDDI_URL = "http://localhost:8081";
	public static final String DEFAULT_UDDI_NAME = "sd-store";
	
	public String uddiURL;
	public String uddiName;
	private SDStore _port = null;
	
	
	public StoreClient() throws JAXRException{
		this.uddiURL = DEFAULT_UDDI_URL;
		this.uddiName = DEFAULT_UDDI_NAME;
		
		_port = findUddi(uddiURL, uddiName);
	}
	
	
	public StoreClient(String uddiUrl, String uddiName) throws JAXRException{
		this.uddiURL = uddiUrl;
		this.uddiName = uddiName;
		
		_port = findUddi(uddiURL, uddiName);
	}
	
	
	public static SDStore createPort(){
		SDStore_Service service = new SDStore_Service();
		return service.getSDStoreImplPort();
	}
	
	public SDStore getPort(){
		return _port;
	}
	
	
	public void initIfNoPort(){
		if(_port == null){
			try {
				_port = findUddi(uddiURL, uddiName);
			} catch (JAXRException e) {
				return;
			}
		}
	}
	
	
	public SDStore findUddi(String uddiURL, String uddiName) throws JAXRException {
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
	
	
	public void createDoc(String userID, String docID) throws InvalidAttributeValueException, DocAlreadyExists_Exception{
		CreateDocService service = new CreateDocService(userID, docID, _port);
		service.dispatch();
	}
	
	
	public List<String> listDocs(String userID) throws InvalidAttributeValueException, UserDoesNotExist_Exception{
		ListDocsService service = new ListDocsService(userID, _port);
		service.dispatch();
		return service.getResult();
	}
	
	
	public byte[] loadDoc(String userID, String docID) throws InvalidAttributeValueException, DocDoesNotExist_Exception, UserDoesNotExist_Exception{
		LoadDocService service = new LoadDocService(userID, docID, _port);
		service.dispatch();
		return service.getResult();
	}
	
	
	public void storeDoc(String userID, String docID, byte[] content) throws InvalidAttributeValueException, CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception{
		StoreDocService service = new StoreDocService(userID, docID, content, _port);
		service.dispatch();
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
