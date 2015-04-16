
package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.exceptions.DuplicateEmailException;
import pt.tecnico.bubbledocs.exceptions.InvalidEmailException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;



public class CreateUserTest extends BubbleDocsServiceTest {

    @Mocked
    IDRemoteServices remote;	
	
    // the tokens
    private String root_token;
    private String user_token;

    private static final String USERNAME = "turtle";
    private static final String PASSWORD = "pizza";
    private static final String EMAIL = "Nets@playoffs.com";
	private static final String EMAIL_PRESENT = "Celtics@playoffs.com";
	private static final String USERNAME_PRESENT = "rato";
    private static final String NAME = "Franklin" ; 
    private static final String ROOT_USERNAME = "root";


    @Override
    public void populate4Test() {
		createUser(ROOT_USERNAME, "ROOT@ROOT.COM", PASSWORD, "Super User");
    	root_token = addUserToSession(ROOT_USERNAME);
       createUser(USERNAME_PRESENT, EMAIL_PRESENT, PASSWORD, NAME);
        user_token = addUserToSession(USERNAME_PRESENT);
    }

    @Test
    public void success() {
        CreateUser service = new CreateUser(root_token, USERNAME, EMAIL, NAME);
        service.execute();

	// User is the domain class that represents a User
        User user = service.getResult();

        new Verifications() {{ //verify the service was called
			remote.createUser(USERNAME, EMAIL); times = 1;
		}};
        
        assertEquals(USERNAME, user.getUserName());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(NAME, user.getName());
    }

    @Test(expected =  DuplicateUsernameException.class)
    public void usernameExists() {
    	new Expectations() {{
    		remote.createUser(USERNAME_PRESENT, EMAIL); times = 1;
			result = new DuplicateUsernameException();
    	}};
    	
        CreateUser service = new CreateUser(root_token, USERNAME_PRESENT, EMAIL, NAME);
        service.execute();
    }

    //TODO: empty username: InvalidValueException or InvalidUserNameException ???
    @Test(expected = InvalidUsernameException.class)
    public void emptyUsername() {
        CreateUser service = new CreateUser(root_token, "", EMAIL, NAME);
        service.execute();
        
        new Verifications() {{
        	remote.createUser(anyString, anyString); times = 0;
        }};
    }

	
	@Test(expected = InvalidUsernameException.class)
    public void usernameTooLong(){
    	String long_name = "Maria Teresa García Ramírez de Arroyo";
    	CreateUser service = new CreateUser(root_token, long_name, EMAIL, "name");
    	service.execute();
    	
        new Verifications() {{
        	remote.createUser(anyString, anyString); times = 0;
        }};
    }
	
	
    @Test(expected = InvalidUsernameException.class)
    public void usernameTooShort(){
    	CreateUser service = new CreateUser(root_token, "a", EMAIL, "name");
    	service.execute();
    	
        new Verifications() {{
        	remote.createUser(anyString, anyString); times = 0;
        }};
    }
	
    @Test(expected = PermissionException.class)
    public void unauthorizedUserCreation() {
        CreateUser service = new CreateUser(user_token, USERNAME, "queijo",
                "Sensei");
        service.execute();
        
        new Verifications() {{
        	remote.createUser(anyString, anyString); times = 0;
        }};
    }

    @Test(expected = UserNotInSessionException.class)
    public void NotInSession() {
        removeUserFromSession(root_token);
        CreateUser service = new CreateUser(root_token, USERNAME, "queijo",
                "Sensei");
        service.execute();
        
        new Verifications() {{
        	remote.createUser(anyString, anyString); times = 0;
        }};
    }
    
    
    @Test(expected = DuplicateEmailException.class)
    public void DuplicateEmail(){
    	new Expectations() {{
    		remote.createUser(USERNAME,EMAIL); times = 1;
    		result = new DuplicateEmailException();
    	}};

    	CreateUser service = new CreateUser(root_token, USERNAME, EMAIL, NAME);
    	service.execute();
    }

    @Test(expected = InvalidEmailException.class)
    public void InvalidEmail(){

    	new Expectations() {{
    		remote.createUser(USERNAME,"im no mail"); times = 1;
    		result = new InvalidEmailException();
    	}};

    	CreateUser service = new CreateUser(root_token, USERNAME, "im no mail", NAME);
    	service.execute();

    }

    @Test(expected = UnavailableServiceException.class)
	public void unavailable() {
		new Expectations() {{
			remote.createUser(USERNAME, EMAIL); times = 1;
			result = new RemoteInvocationException();
		}};
		
		CreateUser service = new CreateUser(root_token, USERNAME, EMAIL, NAME);
		service.execute();
		//fail("FIXME");
	}

}
