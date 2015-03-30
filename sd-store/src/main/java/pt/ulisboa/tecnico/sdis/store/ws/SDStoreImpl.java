package pt.ulisboa.tecnico.sdis.store.ws;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jws.WebService;

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
			SDStoreMain.getDB().addDoc(docUserPair);
		}
		//TODO: catch? throw? think about it...
		catch(DocAlreadyExists_Exception daeExcept){
			throw daeExcept;
		}
	}

	
	@Override
	public List<String> listDocs(String userId)
			throws UserDoesNotExist_Exception {
		// TODO Auto-generated method stub
		
		ArrayList<String> userDocs = new ArrayList<String>();
		Collection<Doc> allDocs = SDStoreMain.getDB().getAllDocs();
		
		for(Doc d: allDocs){
			String owner = d.getOwner();
			
			if (owner.equals(userId)){
				userDocs.add(owner);
			}
		}
		
		return null;
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
