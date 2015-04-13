package pt.tecnico.sd.id;

//This class implements the service

import java.security.SecureRandom;

import javax.jws.*;

import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser;
import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

@WebService(
    endpointInterface="pt.ulisboa.tecnico.sdis.id.ws.SDId", 
    wsdlLocation="sdid.wsdl",
    name="SdId",
    portName="SDIdImplPort",
    targetNamespace="urn:pt:ulisboa:tecnico:sdis:id:ws",
    serviceName="SDId"
)
public class SDIdImpl implements SDId {

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
	
	public SDIdImpl() {
		this.userTable = new UserTable();
		this.rng = new SecureRandom();
	}
	
	//to be used in the sd-id tests
	protected SDIdImpl(UserTable users) throws Exception {
		this.userTable = users;
		this.rng = new SecureRandom();
		this.populateForTest(userTable);
	}
	
	private void populateForTest(UserTable userTable) throws Exception {
		userTable.addUser(userName1, email1, password1);
		userTable.addUser(userName2, email2, password2);
		userTable.addUser(userName3, email3, password3);
		userTable.addUser(userName4, email4, password4);
		userTable.addUser(userName5, email5, password5);
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
		

		if(!emailIsValid(emailAddress)){
			InvalidEmail fault = new InvalidEmail();
			fault.setEmailAddress(emailAddress);
			throw new InvalidEmail_Exception(emailAddress + " is invalid", fault);
		}
		
		userTable.addUser(userId,emailAddress, generateRandomPassword());
		
	}

	@Override
	public void renewPassword(String userId) throws UserDoesNotExist_Exception {
		byte[] newPassword = generateRandomPassword();
		userTable.changePassword(userId, newPassword);
	}

	@Override
	public void removeUser(String userId) throws UserDoesNotExist_Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] requestAuthentication(String userId, byte[] reserved)
			throws AuthReqFailed_Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	private byte[] generateRandomPassword() {
		StringBuilder builder = new StringBuilder();
		
		//ten digits and 52 letters, both upper and lower case.
		//Java 8 streams and lambdas for the win!
		rng.ints(0, 62).limit(8)
		               .map(x -> {
			if      (x < 10)            return '0'+x;
			else if (x >= 10 && x < 36) return 'a'+x-10;
			else                        return 'A'+x-36;
		}).forEach(x -> builder.append((char) x));
		
		return builder.toString().getBytes();
	}
	
	protected UserTable getTable() {
		return userTable;
	}
	
	protected User getUserByUsername(String username) {
		return userTable.getUserByUsername(username);
	}

}
