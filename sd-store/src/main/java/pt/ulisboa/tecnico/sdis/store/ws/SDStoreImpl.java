package pt.ulisboa.tecnico.sdis.store.ws;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import pt.ulisboa.tecnico.sdis.store.service.CreateDocService;

@WebService(
	endpointInterface="pt.ulisboa.tecnico.sdis.store.ws.SDStore",
	wsdlLocation="SD-Store.1_1.wsdl",
	name="SDStore",
	portName="SDStoreImplPort",
	targetNamespace="urn:pt:ulisboa:tecnico:sdis:store:ws",
	serviceName="SdStore"
)

public class SDStoreImpl implements SDStore{

	@Override
	public void createDoc(DocUserPair docUserPair)
			throws DocAlreadyExists_Exception {
		
		try{
			CreateDocService service =
					new CreateDocService(docUserPair.getUserId(), docUserPair.getDocumentId());
			
			service.dispatch();
		}
		
		//TODO: catch? throw? think about it...
		catch(DocAlreadyExists_Exception e){
			throw e;
		}
	}

	
	@Override
	public List<String> listDocs(String userID)
			throws UserDoesNotExist_Exception {
		// TODO Auto-generated method stub
		
		Storage storage = SDStoreMain.getStorage();
		
		List<Doc> docsList = storage.getUserDocs(userID);
		
		if(docsList == null){
			UserDoesNotExist udneM = new UserDoesNotExist();
			udneM.setUserId(userID);
			
			throw new UserDoesNotExist_Exception("User does not exist", udneM);
		}
		
		ArrayList<String> idsList = new ArrayList<String>();
		
		for(Doc d: docsList){
			idsList.add(d.getDocID());
		}
		
		return idsList;
	}

	
	@Override
	public void store(DocUserPair docUserPair, byte[] contents)
			throws CapacityExceeded_Exception, DocDoesNotExist_Exception,
			UserDoesNotExist_Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] load(DocUserPair docUserPair)
			throws DocDoesNotExist_Exception, UserDoesNotExist_Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
