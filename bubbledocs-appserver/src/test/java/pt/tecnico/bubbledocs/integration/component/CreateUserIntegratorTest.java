
package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;
import mockit.Mocked;
import mockit.Expectations;
import mockit.Verifications;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.service.BubbleDocsServiceTest;
import pt.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.exceptions.DuplicateEmailException;
import pt.tecnico.bubbledocs.exceptions.InvalidEmailException;
import pt.tecnico.bubbledocs.integration.CreateUserIntegrator;



public class CreateUserIntegratorTest extends BubbleDocsServiceTest {

	@Mocked
	IDRemoteServices remote;
	
	//tokens
    private String root_token;
    private String user_token;
	//info for user creation
    private static final String USERNAME = "turtle";
    private static final String PASSWORD = "pizza";
    private static final String EMAIL = "Nets@playoffs.com";
	private static final String EMAIL_PRESENT = "Celtics@playoffs.com";
	private static final String USERNAME_PRESENT = "rato";
    private static final String NAME = "Franklin" ; 
    private static final String ROOT_USERNAME = "root";

	
    @Override
    public void populate4Test() {
		//createUser(ROOT_USERNAME, EMAIL, "rootroot", "supah");
    	root_token = addUserToSession(ROOT_USERNAME);
    	createUser(USERNAME_PRESENT, EMAIL_PRESENT, PASSWORD, NAME);
        user_token = addUserToSession(USERNAME_PRESENT);
    }
    
	//success case
    @Test
    public void success() {
    	CreateUserIntegrator service = new CreateUserIntegrator(root_token, USERNAME, EMAIL, NAME);
        service.execute();
        
        new Verifications() {{ //verify the service was called		
        	remote.createUser(USERNAME, EMAIL);	times =  1;
        }};
        
        User user = service.getResult();
        
        assertEquals(USERNAME, user.getUserName());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(NAME, user.getName());
    }
    
    //null username case
    @Test(expected = InvalidUsernameException.class)
    public void emptyUsername() {
    	String long_name = "";    	
    	CreateUserIntegrator service = new CreateUserIntegrator(root_token, long_name, EMAIL, "name");
    	service.execute();
    	
    	new Verifications() {{
    		remote.createUser(anyString, anyString); times = 0;
    	}};
    }
    
    //duplicate username case
    @Test(expected =  DuplicateUsernameException.class)
    public void usernameExists() {
    	new Expectations() {{
        	remote.createUser(USERNAME_PRESENT, EMAIL);
        	result = new DuplicateUsernameException();
        }};
    	
    	CreateUserIntegrator service = new CreateUserIntegrator(root_token, USERNAME_PRESENT, EMAIL, NAME);
        service.execute();        
    }
    
    //long username case
    @Test(expected = InvalidUsernameException.class)
    public void usernameTooLong(){
    	String long_name = "Maria Teresa García Ramírez de Arroyo";    	
    	CreateUserIntegrator service = new CreateUserIntegrator(root_token, long_name, EMAIL, "name");
    	service.execute();
    	
    	new Verifications() {{
    		remote.createUser(anyString, anyString); times = 0;
    	}};
    }
    
    //short username case
    @Test(expected = InvalidUsernameException.class)
    public void usernameTooShort(){
    	CreateUserIntegrator service = new CreateUserIntegrator(root_token, "a", EMAIL, "name");
    	service.execute();
    	
    	new Verifications() {{
    		remote.createUser(anyString, anyString); times = 0;
    	}};    	
    }
    
    //user with no permission case
    @Test(expected = PermissionException.class)
    public void unauthorizedUserCreation() {
        CreateUserIntegrator service = new CreateUserIntegrator(user_token, USERNAME, "queijo",
                "Sensei");
        service.execute();
        
        new Verifications() {{
    		remote.createUser(anyString, anyString); times = 0;
    	}};        
    }
    
    //root not in session case
    @Test(expected = UserNotInSessionException.class)
    public void NotInSession() {
        removeUserFromSession(root_token);
        CreateUserIntegrator service = new CreateUserIntegrator(root_token, USERNAME, "queijo",
                "Sensei");
        service.execute();
        
        new Verifications() {{
    		remote.createUser(anyString, anyString); times = 0;
    	}};        
    }
    
    //duplicate username case
    @Test(expected = DuplicateEmailException.class)
    public void DuplicateEmail(){
    	new Expectations() {{
    		remote.createUser(USERNAME, EMAIL_PRESENT);
    		result = new DuplicateEmailException();
    	}};
    	
    	CreateUserIntegrator service = new CreateUserIntegrator(root_token, USERNAME, EMAIL_PRESENT, NAME);
    	service.execute();
    }

    //invalid email case
    @Test(expected = InvalidEmailException.class)
    public void InvalidEmail(){
    	new Expectations() {{
    		remote.createUser(USERNAME, "im no mail");
    		result = new InvalidEmailException();
    	}};
    	
    	CreateUserIntegrator service = new CreateUserIntegrator(root_token, USERNAME, "im no mail", NAME);
    	service.execute();
    }
    
    //remote service exception case
    @Test(expected = UnavailableServiceException.class)
	public void unavailable() {
    	new Expectations() {{
    		remote.createUser(USERNAME, EMAIL);
    		result = new RemoteInvocationException();
    	}};
    	
		CreateUserIntegrator service = new CreateUserIntegrator(root_token, USERNAME, EMAIL, NAME);
		service.execute();
	}
    


}
