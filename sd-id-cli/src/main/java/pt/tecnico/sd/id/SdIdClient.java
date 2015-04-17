package pt.tecnico.sd.id;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import example.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.SDId_Service;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

public class SdIdClient {
    private static SdIdClient instance = null;
    private SDId port;

    private SdIdClient() throws JAXRException {
    	String uddiUrl = System.getProperty("uddi.url");
		String wsName = System.getProperty("ws.name");
		
	    System.out.printf("Contacting UDDI at %s%n", uddiUrl);
	    UDDINaming uddiNaming = new UDDINaming(uddiUrl);

	    System.out.printf("Looking for '%s'%n", wsName);
	    String endpointAddress = uddiNaming.lookup(wsName);

	    if (endpointAddress == null) {
		System.out.println("Not found!");
		throw new RuntimeException("endpoint address not found");
	    } else {
		System.out.printf("Found %s%n", endpointAddress);
	    }
    	
    	 SDId_Service service = new SDId_Service(); 
 	    this.port = service.getSDIdImplPort();
 	
 	    System.out.println("Setting endpoint address ...");
 	    BindingProvider bindingProvider = (BindingProvider) port;
 	    Map<String, Object> requestContext = bindingProvider.getRequestContext();
 	    requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);	  

     
    }

    public static SdIdClient getInstance() throws Exception {
    	if (instance == null) {
    		try {
    			instance = new SdIdClient();
    		} catch (/*Some*/Exception e) {
    			instance = null;
    			throw e;
    		}
    	}

	return instance;
    }
    
    public void createUser(String userId, String emailAddress) throws EmailAlreadyExists_Exception, InvalidEmail_Exception,
	InvalidUser_Exception, UserAlreadyExists_Exception {
    	port.createUser(userId, emailAddress);
    }
    
    public void renewPassword(String userId) throws UserDoesNotExist_Exception {
    	port.renewPassword(userId);
    }
    
    public void removeUser(String userId) throws UserDoesNotExist_Exception {
    	port.removeUser(userId);
    }
    
    public byte[] requestAuthentication(String userId, byte[] reserved) throws AuthReqFailed_Exception{
    	return port.requestAuthentication( userId,reserved);
    }
}
