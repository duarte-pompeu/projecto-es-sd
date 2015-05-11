package pt.ulisboa.tecnico.sdis.store.ws;

import java.util.List;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

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
@HandlerChain(file="/handler-chain.xml")
public class SDStoreImpl implements SDStore{
//	@Resource
//    private WebServiceContext webServiceContext;
	
	public final boolean DEBUG;
	
	public SDStoreImpl(){
		DEBUG = false;
	}
	
	public SDStoreImpl(boolean debug){
		DEBUG = true;
	}
	
	
	@Override
	public void createDoc(DocUserPair docUserPair)
		throws DocAlreadyExists_Exception {
		
		String userID = docUserPair.getUserId();
		String docID = docUserPair.getDocumentId();
		String clientID = SDStoreMain.RECEIVED_CLIENT_ID;
		
		CreateDocService service = 	new CreateDocService(userID, docID, clientID);
		service.dispatch();
		
		if(DEBUG){
			System.out.printf("User '%s' created doc '%s'.\n", userID, docID);
			System.out.printf("SEQ: %d , USER: %d\n", service.getSeq(), service.getUserNumber());
			System.out.println("USER_ID: " + clientID);
		}
	}

	
	@Override
	public List<String> listDocs(String userID)
			throws UserDoesNotExist_Exception {
	
		ListDocsService service = new ListDocsService(userID);
		String clientID = SDStoreMain.RECEIVED_CLIENT_ID;
		
		service.dispatch();
		
		List<String> result = service.getResult();
		
		if(DEBUG){
			System.out.printf("User '%s' listed his collection of '%d' docs.\n", userID, result.size());
			
			for(String doc: result){
				System.out.println("   * " + doc);
			}
			System.out.printf("TAG: SEQ: %d , USER: %d\n", service.getSeq(), service.getUserNumber());
			System.out.println("USER_ACCESS: " + clientID);
		}
		
		return result;
	}

	
	@Override
	public void store(DocUserPair docUserPair, byte[] contents)
			throws CapacityExceeded_Exception, DocDoesNotExist_Exception,
			UserDoesNotExist_Exception {
		
		String userID = docUserPair.getUserId();
		String docID = docUserPair.getDocumentId();
		String clientID = SDStoreMain.RECEIVED_CLIENT_ID;
		
		StoreService service = new StoreService(userID, docID, contents, clientID);
		service.dispatch();
		
		if(DEBUG){
			System.out.printf("User '%s' stored '%d' bytes in doc '%s'.\n", userID, contents.length, docID);
			System.out.printf("TAG: SEQ: %d , USER: %d\n", service.getSeq(), service.getUserNumber());
			System.out.println("USER_ACCESS: " + clientID);
		}
	}

	@Override
	public byte[] load(DocUserPair docUserPair)
			throws DocDoesNotExist_Exception, UserDoesNotExist_Exception {
		
		String userID = docUserPair.getUserId();
		String docID = docUserPair.getDocumentId();
		String clientID = SDStoreMain.RECEIVED_CLIENT_ID;
		
		LoadService service = new LoadService(userID, docID);
		service.dispatch();
		byte result[] = service.getResult();
		
		if(DEBUG){
			System.out.printf("User '%s' loaded '%d' bytes from doc '%s'.\n", userID, result.length, docID);
			System.out.printf("TAG: SEQ: %d , USER: %d\n", service.getSeq(), service.getUserNumber());
			System.out.println("USER_ACCESS: " + clientID);
		}
		
//		MessageContext messageContext = webServiceContext.getMessageContext();
//		messageContext.put(StoreHeaderHandler.CONTEXT_PROPERTY, "test");//service.getTag());
		return result;
	}
}
