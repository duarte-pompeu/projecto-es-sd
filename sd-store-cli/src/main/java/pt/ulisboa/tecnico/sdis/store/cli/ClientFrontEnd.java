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
	private QuorumFactory qFact;
	
	public ClientFrontEnd(String uddiName) throws JAXRException{
		this._uddiName = uddiName;
		init();
		
		int n_servers = _clients.size();
		qFact = new QuorumFactory(n_servers);
	}
	
	public ClientFrontEnd() throws JAXRException{
		this(DEFAULT_UDDI_NAME);
	}
	
	public void setThresholds(int rt, int wt){
		
		int nVoters = _clients.size();
		this.qFact = new QuorumFactory(nVoters, rt, wt);
	}
	
	
	public void init() throws JAXRException{
		if(VERBOSE){
			System.out.println("LOOKING FOR ENDPOINT ADDRESSES...");
		}
		
		for(String endpointAddress: listEndpoints()){
			_clients.add(initClient(endpointAddress));
			
			if(VERBOSE){
				System.out.println("   * " + endpointAddress);
			}
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
	

	public void createDoc(String userID, String docID) throws InvalidAttributeValueException, DocAlreadyExists_Exception{
		
		for(StoreClient client : _clients){
			client.createDoc(userID, docID);
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
	
	public void storeDoc(String userID, String docID, byte[] content) throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception {
		Quorum quorum = qFact.getNewWriteQuorum();
		
		for(StoreClient client : _clients){
			
			try {
				client.storeDoc(userID, docID, content);
				quorum.addSuccess();
				
			} catch (InvalidAttributeValueException e) {
				quorum.addException(e);
			} catch (CapacityExceeded_Exception e) {
				quorum.addException(e);
			} catch (DocDoesNotExist_Exception e) {
				quorum.addException(e);
			} catch (UserDoesNotExist_Exception e) {
				quorum.addException(e);
			}
		}
		
		quorum.getVerdict();
	}
	

	public byte[] loadDoc(String userID, String docID) throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception{
		Quorum quorum = qFact.getNewReadQuorum();
		
		byte[] res;
		for(StoreClient client : _clients){
			try {
				res = client.loadDoc(userID, docID);
				quorum.addResponse(res);
			} 
			
			catch (InvalidAttributeValueException e) {
				quorum.addException(e);
			} catch (DocDoesNotExist_Exception e) {
				quorum.addException(e);
			} catch (UserDoesNotExist_Exception e) {
				quorum.addException(e);
			}
		}
		
		return quorum.getVerdict();
	}
}
