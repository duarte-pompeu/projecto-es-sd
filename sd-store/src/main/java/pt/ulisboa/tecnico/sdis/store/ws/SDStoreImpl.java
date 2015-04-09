package pt.ulisboa.tecnico.sdis.store.ws;

import java.util.List;

import javax.jws.WebService;

import pt.ulisboa.tecnico.sdis.store.service.CreateDocService;
import pt.ulisboa.tecnico.sdis.store.service.ListDocsService;
import pt.ulisboa.tecnico.sdis.store.service.LoadService;
import pt.ulisboa.tecnico.sdis.store.service.StoreService;

@WebService(
	endpointInterface="pt.ulisboa.tecnico.sdis.store.ws.SDStore",
	wsdlLocation="SD-STORE.1_1.wsdl",
	name="SDStore",
	portName="SDStoreImplPort",
	targetNamespace="urn:pt:ulisboa:tecnico:sdis:store:ws",
	serviceName="SDStore"
)

public class SDStoreImpl implements SDStore{

	@Override
	public void createDoc(DocUserPair docUserPair)
		throws DocAlreadyExists_Exception {
	
		CreateDocService service = 	new CreateDocService(docUserPair.getUserId(), docUserPair.getDocumentId());
		service.dispatch();
	}

	
	@Override
	public List<String> listDocs(String userID)
			throws UserDoesNotExist_Exception {
		
		ListDocsService service = new ListDocsService(userID);
		service.dispatch();
		return service.getResult();
	}

	
	@Override
	public void store(DocUserPair docUserPair, byte[] contents)
			throws CapacityExceeded_Exception, DocDoesNotExist_Exception,
			UserDoesNotExist_Exception {
		
		StoreService service = new StoreService(docUserPair.getUserId(), docUserPair.getDocumentId(), contents);
		service.dispatch();
	}

	@Override
	public byte[] load(DocUserPair docUserPair)
			throws DocDoesNotExist_Exception, UserDoesNotExist_Exception {
		
		LoadService service = new LoadService(docUserPair.getUserId(), docUserPair.getDocumentId());
		service.dispatch();
		return service.getResult();
	}
}
