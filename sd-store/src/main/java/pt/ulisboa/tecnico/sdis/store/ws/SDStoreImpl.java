package pt.ulisboa.tecnico.sdis.store.ws;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(
	endpointInterface="pt.ulisboa.tecnico.sdis.store.ws.SDStore",
	wsdlLocation="SD-Store.1_1.wsdl",
	name="SDStore",
	portName="SDStoreImplPort",
	targetNamespace="urn:pt:ulisboa:tecnico:sdis:store:ws",
	serviceName="SdStore"
)

public class SDStoreImpl implements SDStore{
	
    /**
     * 
     * @param docUserPair
     * @throws DocAlreadyExists_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "createDoc", targetNamespace = "urn:pt:ulisboa:tecnico:sdis:store:ws", className = "pt.ulisboa.tecnico.sdis.store.ws.CreateDoc")
    @ResponseWrapper(localName = "createDocResponse", targetNamespace = "urn:pt:ulisboa:tecnico:sdis:store:ws", className = "pt.ulisboa.tecnico.sdis.store.ws.CreateDocResponse")
    public void createDoc(
        @WebParam(name = "docUserPair", targetNamespace = "")
        DocUserPair docUserPair)
        throws DocAlreadyExists_Exception
    {}

    /**
     * 
     * @param userId
     * @return
     *     returns java.util.List<java.lang.String>
     * @throws UserDoesNotExist_Exception
     */
    @WebMethod
    @WebResult(name = "documentId", targetNamespace = "")
    @RequestWrapper(localName = "listDocs", targetNamespace = "urn:pt:ulisboa:tecnico:sdis:store:ws", className = "pt.ulisboa.tecnico.sdis.store.ws.ListDocs")
    @ResponseWrapper(localName = "listDocsResponse", targetNamespace = "urn:pt:ulisboa:tecnico:sdis:store:ws", className = "pt.ulisboa.tecnico.sdis.store.ws.ListDocsResponse")
    public List<String> listDocs(
        @WebParam(name = "userId", targetNamespace = "")
        String userId)
        throws UserDoesNotExist_Exception
    {
		return null;
	}

    /**
     * 
     * @param contents
     * @param docUserPair
     * @throws UserDoesNotExist_Exception
     * @throws DocDoesNotExist_Exception
     * @throws CapacityExceeded_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "store", targetNamespace = "urn:pt:ulisboa:tecnico:sdis:store:ws", className = "pt.ulisboa.tecnico.sdis.store.ws.Store")
    @ResponseWrapper(localName = "storeResponse", targetNamespace = "urn:pt:ulisboa:tecnico:sdis:store:ws", className = "pt.ulisboa.tecnico.sdis.store.ws.StoreResponse")
    public void store(
        @WebParam(name = "docUserPair", targetNamespace = "")
        DocUserPair docUserPair,
        @WebParam(name = "contents", targetNamespace = "")
        byte[] contents)
        throws CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception
    {
	}

    /**
     * 
     * @param docUserPair
     * @return
     *     returns byte[]
     * @throws UserDoesNotExist_Exception
     * @throws DocDoesNotExist_Exception
     */
    @WebMethod
    @WebResult(name = "contents", targetNamespace = "")
    @RequestWrapper(localName = "load", targetNamespace = "urn:pt:ulisboa:tecnico:sdis:store:ws", className = "pt.ulisboa.tecnico.sdis.store.ws.Load")
    @ResponseWrapper(localName = "loadResponse", targetNamespace = "urn:pt:ulisboa:tecnico:sdis:store:ws", className = "pt.ulisboa.tecnico.sdis.store.ws.LoadResponse")
    public byte[] load(
        @WebParam(name = "docUserPair", targetNamespace = "")
        DocUserPair docUserPair)
        throws DocDoesNotExist_Exception, UserDoesNotExist_Exception
    {
		return null;
	}
}
