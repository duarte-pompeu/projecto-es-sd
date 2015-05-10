package pt.tecnico.sd.id.cli;

import java.util.Arrays;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

import pt.tecnico.sd.ClientTicket;
import pt.tecnico.sd.SdCrypto;
import pt.tecnico.sd.SdCryptoException;
import pt.tecnico.sd.id.cli.handler.TicketHandler;
import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

public class SdIdClient {
	private static SdIdClient instance = null;
	private SdIdConnector connector = new SdIdConnector();
	private SDId port;

	protected SdIdClient() throws SdIdRemoteException {
		try {
			String uddiUrl = System.getProperty("uddi.url");
			String wsName = System.getProperty("sd.id.name");
			
			connector.connect(uddiUrl, wsName);			
			this.port = connector.getPort();			
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

	public ClientTicket requestAuthentication(String userId, byte[] password) throws AuthenticationException, SdIdRemoteException {
		try {
			SecretKey key = SdCrypto.generateKey(SdCrypto.digestPassword(password));
			String reserved = "SD-Store:123456789";
			byte[] encrypted = port.requestAuthentication(userId, reserved.getBytes());			
			byte[] credentials = SdCrypto.decrypt(key, encrypted);
			byte[] authenticator = Arrays.copyOfRange(credentials, 4, credentials.length);
			
			Map<String, Object> context = connector.getResponseContext();
			String ticketBlob = (String) context.get(TicketHandler.TICKET_PROPERTY);
			return new ClientTicket(ticketBlob, authenticator);

		} catch (WebServiceException e) {
			throw new SdIdRemoteException(e);
		} catch (AuthReqFailed_Exception | SdCryptoException e) {
			throw new AuthenticationException("Failed authentication");
		}
	}
}
