package pt.tecnico.sd.id;

import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

public class SdIdClient {
    private static SdIdClient instance = null;
    private SDId port;

    public SdIdClient() {
    	// is it something like this that I'm supposed to do???
    	SDIdImpl service = new SDIdImpl();
        SDId port = service.getSDIdImplPort();

        BindingProvider bindingProvider = (BindingProvider) port;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();

        Object url = requestContext.get(ENDPOINT_ADDRESS_PROPERTY);
        System.out.printf("Remote call to %s ...%n", url);

     
    }

    public static SdIdClient getInstance() {
	if (instance == null) {
	    try {
		instance = new SdIdClient();
	    } catch (/*Some*/Exception e) {
		instance = null;
		throw new /*Some*/Exception();
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
