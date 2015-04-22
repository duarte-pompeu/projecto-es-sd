package pt.tecnico.sd.id.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Map;
import java.util.logging.*;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

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

	protected SdIdClient() throws SdIdRemoteException {
		try {
			String uddiUrl = System.getProperty("uddi.url");
			String wsName = System.getProperty("ws.name");

			Logger logger = Logger.getLogger("pt.tecnico.ulisboa.essd.sd-id-cli");

			logger.info("Contacting UDDI at " + uddiUrl);
			UDDINaming uddiNaming = new UDDINaming(uddiUrl);

			logger.info("Looking for " + wsName);
			String endpointAddress = uddiNaming.lookup(wsName);

			if (endpointAddress == null) {
				logger.severe("Endpoint not found!");
				throw new SdIdRemoteException("endpoint address not found");
			} else {
				logger.info("Found " + endpointAddress);
			}

			SDId_Service service = new SDId_Service(); 
			this.port = service.getSDIdImplPort();

			logger.info("Setting endpoint address ...");
			BindingProvider bindingProvider = (BindingProvider) port;
			Map<String, Object> requestContext = bindingProvider.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
		} catch (WebServiceException | JAXRException e) {
			throw new SdIdRemoteException(e);
		}
	}

	public static SdIdClient getInstance() throws SdIdRemoteException {
		if (instance == null) {
			try {
				instance = new SdIdClient();
			} catch (SdIdRemoteException e) {
				instance = null;
				throw e;
			}
		}

		return instance;
	}

	public void createUser(String userId, String emailAddress) throws EmailAlreadyExists_Exception, InvalidEmail_Exception,
	InvalidUser_Exception, UserAlreadyExists_Exception, SdIdRemoteException {
		try {
			port.createUser(userId, emailAddress);
		} catch (WebServiceException e) {
			throw new SdIdRemoteException(e);
		}
	}

	public void renewPassword(String userId) throws UserDoesNotExist_Exception, SdIdRemoteException {
		try {
			port.renewPassword(userId);
		} catch (WebServiceException e) {
			throw new SdIdRemoteException(e);
		}
	}

	public void removeUser(String userId) throws UserDoesNotExist_Exception, SdIdRemoteException {
		try {
			port.removeUser(userId);
		} catch (WebServiceException e) {
			throw new SdIdRemoteException(e);
		}
	}

	public byte[] requestAuthentication(String userId, byte[] reserved) throws AuthReqFailed_Exception, SdIdRemoteException {
		try {
			return port.requestAuthentication( userId,reserved);
		} catch (WebServiceException e) {
			throw new SdIdRemoteException(e);
		}
	}
}
