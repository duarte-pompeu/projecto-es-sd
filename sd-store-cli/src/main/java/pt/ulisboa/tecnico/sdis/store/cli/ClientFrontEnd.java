package pt.ulisboa.tecnico.sdis.store.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.naming.directory.InvalidAttributeValueException;
import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.juddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;

public class ClientFrontEnd {
	private static final String DEFAULT_UDDI_NAME = "SD-STORE";
	private static final String DEFAULT_UDDI_URL = "http://localhost:8081";
	
	private static boolean VERBOSE = true;
	
	private static Collection<StoreClient> _clients = new ArrayList<StoreClient>();
	private String _uddiName = "SD-STORE";
	
	
	public ClientFrontEnd(String uddiName) throws JAXRException{
		this._uddiName = uddiName;
		init();
	}
	
	public ClientFrontEnd() throws JAXRException{
		this(DEFAULT_UDDI_NAME);
	}
	
	public void createDoc(String userID, String docID) throws InvalidAttributeValueException, DocAlreadyExists_Exception{
		for(StoreClient client : _clients){
			client.createDoc(userID, docID);
		}
	}
	
	
	public void init() throws JAXRException{
		if(VERBOSE){
			System.out.println("ENDPOINT ADDRESSES ... START ...");
		}
		
		for(String endpointAddress: listEndpoints()){
			_clients.add(initClient(endpointAddress));
			
			if(VERBOSE){
				System.out.println(endpointAddress);
			}
		}
		
		if(VERBOSE){
			System.out.println("ENDPOINT ADDRESSES ... END ...");
		}
	}
	
	
	public StoreClient initClient(String endpointAddress){
		SDStore_Service service = new SDStore_Service();
        SDStore port = service.getSDStoreImplPort();
        
        System.out.println("Setting endpoint address ...");
        BindingProvider bindingProvider = (BindingProvider) port;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
        
        return new StoreClient(port);
	}
	
	
	public Collection<String> listEndpoints() throws JAXRException{
		ArrayList<String> endpoints = new ArrayList<String>();
		UDDINaming uddiNaming = new UDDINaming(DEFAULT_UDDI_URL);
		
		for(String endpointAddress: uddiNaming.list(_uddiName + "-%")){
			endpoints.add(endpointAddress);
		}
		
		return endpoints;
	}
}
