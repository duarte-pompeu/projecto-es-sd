package pt.tecnico.sd.id;

//This class implements the service

import javax.jws.*;

import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
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
	private PasswordGenerator rng;
	
	public SDIdImpl() {
		this.userTable = new UserTable();
		this.rng = new PasswordGenerator();
	}
	
	//to be used in the sd-id tests
	protected SDIdImpl(UserTable users) throws Exception {
		this.userTable = users;
		this.populateForTest(userTable);
	}
	
	private void populateForTest(UserTable userTable) throws Exception {
		userTable.addUser(userName1, email1, password1);
		userTable.addUser(userName2, email2, password2);
		userTable.addUser(userName3, email3, password3);
		userTable.addUser(userName4, email4, password4);
		userTable.addUser(userName5, email5, password5);
	}
	
	
	//here the password is generated automatically
	@Override
	public void createUser(String userId, String emailAddress)
			throws EmailAlreadyExists_Exception, InvalidEmail_Exception,
			InvalidUser_Exception, UserAlreadyExists_Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renewPassword(String userId) throws UserDoesNotExist_Exception {
		// TODO Auto-generated method stub
		
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
		return rng.generatePassword();
	}

}
