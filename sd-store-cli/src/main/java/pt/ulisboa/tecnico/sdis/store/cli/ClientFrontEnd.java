package pt.ulisboa.tecnico.sdis.store.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.naming.directory.InvalidAttributeValueException;
import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.juddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class ClientFrontEnd {
	private static final String DEFAULT_UDDI_NAME = "SD-STORE";
	private static final String DEFAULT_UDDI_URL = "http://localhost:8081";
	
	private static boolean VERBOSE = true;
	
	private static Collection<StoreClient> _clients = new ArrayList<StoreClient>();
	private String _uddiName = "SD-STORE";
	int min4quorum;
	
	
	public ClientFrontEnd(String uddiName) throws JAXRException{
		this._uddiName = uddiName;
		init();
	}
	
	public ClientFrontEnd() throws JAXRException{
		this(DEFAULT_UDDI_NAME);
	}
	
	
	public void init() throws JAXRException{
		if(VERBOSE){
			System.out.println("LOOKING FOR ENDPOINT ADDRESSES...");
		}
	
		int n_servers = 0;
		
		for(String endpointAddress: listEndpoints()){
			_clients.add(initClient(endpointAddress));
			n_servers++;
			
			if(VERBOSE){
				System.out.println("   * " + endpointAddress);
			}
		}
		
		this.min4quorum = (n_servers/2);
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
	
	
	private int getMinForQuorumConsensus() {
		// TODO Auto-generated method stub
		return min4quorum;
	}

	public void createDoc(String userID, String docID) throws InvalidAttributeValueException{
		ArrayList <Exception> exceptions = new ArrayList <Exception>();
		ArrayList <InvalidAttributeValueException> IAVEs = new ArrayList <InvalidAttributeValueException>();
		ArrayList <DocAlreadyExists_Exception> DAEEs = new ArrayList <DocAlreadyExists_Exception>();
		
		for(StoreClient client : _clients){
			try {
				client.createDoc(userID, docID);
			} catch (InvalidAttributeValueException e) {
				exceptions.add(e);
				IAVEs.add(e);
			} catch (DocAlreadyExists_Exception e) {
				exceptions.add(e);
				DAEEs.add(e);
			}
		}
		
		if(exceptions.size() < getMinForQuorumConsensus()){
			// all should be good
			return;
		}
		
		if(IAVEs.size() >= getMinForQuorumConsensus()){
			throw new InvalidAttributeValueException();
		}
		
		if(IAVEs.size() >= getMinForQuorumConsensus()){
			throw IAVEs.get(0);
		}
	}
	
	public Collection<String> listDocs(String userID) throws InvalidAttributeValueException, UserDoesNotExist_Exception {
		ArrayList<String> docs = new ArrayList<String>();
		
		for(StoreClient client: _clients){
			// FIXME: this loop is stupid
			docs = new ArrayList<String>(client.listDocs(userID));
		}
		
		return docs;
	}
	
	public void storeDoc(String userID, String docID, byte[] content) throws InvalidAttributeValueException, CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception {
		for(StoreClient client : _clients){
			client.storeDoc(userID, docID, content);
		}
	}
	

	public byte[] loadDoc(String userID, String docID) throws InvalidAttributeValueException, DocDoesNotExist_Exception, UserDoesNotExist_Exception {
		for(StoreClient client : _clients){
			client.loadDoc(userID, docID);
		}
		return null;
	}
}
