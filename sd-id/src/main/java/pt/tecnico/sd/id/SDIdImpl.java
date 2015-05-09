package pt.tecnico.sd.id;

//This class implements the service

import java.io.FileInputStream;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.annotation.Resource;
import javax.crypto.SecretKey;
import javax.jws.*;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import pt.tecnico.sd.SdCrypto;
import pt.tecnico.sd.Ticket;
import pt.tecnico.sd.id.handler.TicketHandler;
import static javax.xml.bind.DatatypeConverter.printHexBinary;
import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed;
import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser;
import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

@WebService(
    endpointInterface="pt.ulisboa.tecnico.sdis.id.ws.SDId", 
    wsdlLocation="sdid.wsdl",
    name="SdId",
    portName="SDIdImplPort",
    targetNamespace="urn:pt:ulisboa:tecnico:sdis:id:ws",
    serviceName="SDId"
)
@HandlerChain(file="/handler-chain.xml")
public class SDIdImpl implements SDId {

	//The reason for this inner class is to test requestAuthentication.
	class SdIdWebServiceContext {
		public void put(String key, String value) {
			SDIdImpl.this.webServiceContext.getMessageContext().put(key, value);
		}
	}
	
	private final String userName1 = "alice";
	private final String userName2 = "bruno";
	private final String userName3 = "carla";
	private final String userName4 = "duarte";
	private final String userName5 = "eduardo";
	private final byte[] password1 = "Aaa1".getBytes();
	private final byte[] password2 = "Bbb2".getBytes();
	private final byte[] password3 = "Ccc3".getBytes();
	private final byte[] password4 = "Ddd4".getBytes();
	private final byte[] password5 = "Eee5".getBytes();
	private final String email1 = "alice@tecnico.pt";
	private final String email2 = "bruno@tecnico.pt";
	private final String email3 = "carla@tecnico.pt";
	private final String email4 = "duarte@tecnico.pt";
	private final String email5 = "eduardo@tecnico.pt";
	
	private UserTable userTable;
	private SecureRandom rng;
	private SecretKey serviceKey = null; //This is the secret key shared between SD-ID and SD-STORE
	private SdIdWebServiceContext context = new SdIdWebServiceContext();
	
	@Resource
	private WebServiceContext webServiceContext;
	
	public SDIdImpl() {
		this.userTable = new UserTable();
		this.rng = new SecureRandom();
		try {
			this.populateForTest(userTable);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		try (FileInputStream in = new FileInputStream("secret-key")){
			byte[] keyData = new byte[24];
			in.read(keyData);
			this.serviceKey = SdCrypto.generateKey(keyData);
			//log("Key: " + printHexBinary(this.secret.getEncoded()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	//to be used in the sd-id tests
	/*
	protected SDIdImpl(UserTable users) throws Exception {
		this.userTable = users;
		this.rng = new SecureRandom();
		this.populateForTest(userTable);
	}*/
	
	private void populateForTest(UserTable userTable) throws Exception {
		userTable.addUser(userName1, email1, SdCrypto.digestPassword(password1));
		userTable.addUser(userName2, email2, SdCrypto.digestPassword(password2));
		userTable.addUser(userName3, email3, SdCrypto.digestPassword(password3));
		userTable.addUser(userName4, email4, SdCrypto.digestPassword(password4));
		userTable.addUser(userName5, email5, SdCrypto.digestPassword(password5));
	}
	
	
	public boolean emailIsValid(String email){
		String []emailTokens=email.split("@");
		if(emailTokens.length!=2)
			return false;
		else if(emailTokens[0]=="" || emailTokens[1]=="")
			return false;
		else if(email.endsWith("@") || email.startsWith("@"))
			return false;
		
		return true;
	}
	
	//here the password is generated automatically
	@Override
	public void createUser(String userId, String emailAddress) throws EmailAlreadyExists_Exception, InvalidEmail_Exception,
			InvalidUser_Exception, UserAlreadyExists_Exception {
		
		if(userId==null || userId.equals("")){
			InvalidUser fault = new InvalidUser();
			fault.setUserId(userId);
			throw new InvalidUser_Exception(userId + " is invalid", fault);
		}
		

		if(emailAddress == null || !emailIsValid(emailAddress)){
			InvalidEmail fault = new InvalidEmail();
			fault.setEmailAddress(emailAddress);
			throw new InvalidEmail_Exception(emailAddress + " is invalid", fault);
		}
		
		
		
		byte[] password = generateRandomPassword();
		log("Password of " + userId + ": \"" + new String(password) + "\"");
				
		//Store the digested password instead.
		userTable.addUser(userId,emailAddress, 
				SdCrypto.digestPassword(password));
		
	}


	@Override
	public void renewPassword(String userId) throws UserDoesNotExist_Exception {
		byte[] newPassword = generateRandomPassword();
		log("Renewed password for " + userId + ": \"" + new String(newPassword) + "\"");
		userTable.changePassword(userId, SdCrypto.digestPassword(newPassword));
	}

	@Override
	public void removeUser(String userId) throws UserDoesNotExist_Exception  {
		
		if(userId==null || userId.equals("")){
			UserDoesNotExist fault = new UserDoesNotExist();
			fault.setUserId(userId);
			throw new UserDoesNotExist_Exception(userId + " doesnt exist", fault);
		}		
		
		userTable.removeUser(userId);
		
		log(userId + " was removed");
		
	}

	@Override
	public byte[] requestAuthentication(String userId, byte[] reserved)
			throws AuthReqFailed_Exception {
		//If user doesn't exist, throw a AuthReqFailed_Exception
		if (!this.userTable.hasUser(userId)) {
			AuthReqFailed fault = new AuthReqFailed();
			fault.setReserved(reserved);
			throw new AuthReqFailed_Exception("authentication error", fault);
		}
		
		SecretKey sessionKey = SdCrypto.generateRandomKey();
		byte[] sessionKeyData = sessionKey.getEncoded();		
		byte[] credentials = new byte[4 + sessionKeyData.length];
		for (int i=0; i<sessionKeyData.length; ++i) {
			credentials[i+4] = sessionKeyData[i];
		}
		
		SecretKey userKey = SdCrypto.generateKey(this.userTable.getPassword(userId));
		byte[] encryptedCredentials = SdCrypto.encrypt(userKey, credentials);
		
		String ticketBlob = new Ticket(userId, "SD-Store", sessionKey).getBlob(serviceKey);
		
		context.put(TicketHandler.TICKET_PROPERTY, ticketBlob);	
		
		return encryptedCredentials;
	}
	
	private byte[] generateRandomPassword() {
		final int PASSWORD_SIZE = 8;
		
		StringBuilder builder = new StringBuilder();
		
		for (int i=0; i<PASSWORD_SIZE; ++i) {
			int x = rng.nextInt(62);
			int c;
			
			if      (x < 10)            c = '0'+x;
			else if (x >= 10 && x < 36) c = 'a'+x-10;
			else                        c = 'A'+x-36;
			
			builder.append((char) c);
		}
		
		//generate the password digest instead
				
		return builder.toString().getBytes();
	}
	
	protected UserTable getTable() {
		return userTable;
	}
	
	//THIS IS ONLY USED IN TESTS
	protected User getUserByUsername(String username) {
		return userTable.getUserByUsername(username);
	}
	

	private void log(String string) {
		System.out.println(string);
	}
}
