
package pt.tecnico.bubbledocs.integration;

import static org.junit.Assert.assertEquals;
import mockit.Mock;
import mockit.MockUp;
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
	
	
	//Mock class simulating an unavailable SD-ID service
	public static class MockSDIdUnavailable extends MockUp<IDRemoteServices>
	{
	   @Mock
	   public void $init() {}

	   @Mock
	   public void createUser(String username, String email) throws RemoteInvocationException
	   {
	      throw new RemoteInvocationException();
	   }
	}

	//success case
    @Test
    public void success() {
    	CreateUserIntegrator service = new CreateUserIntegrator(root_token, USERNAME, EMAIL, NAME);
        service.execute();

        User user = service.getResult();
        
        assertEquals(USERNAME, user.getUserName());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(NAME, user.getName());
    }
    
    //null username case
    @Test(expected = InvalidUsernameException.class)
    public void emptyUsername() {
        CreateUserIntegrator service = new CreateUserIntegrator(root_token, "", EMAIL, NAME);
        service.execute();
        
    }
    
    //duplicate username case
    @Test(expected =  DuplicateUsernameException.class)
    public void usernameExists() {
        CreateUserIntegrator service = new CreateUserIntegrator(root_token, USERNAME_PRESENT, EMAIL, NAME);
        service.execute();
        
    }
    
    //long username case
    @Test(expected = InvalidUsernameException.class)
    public void usernameTooLong(){
    	String long_name = "Maria Teresa García Ramírez de Arroyo";
    	CreateUserIntegrator service = new CreateUserIntegrator(root_token, long_name, EMAIL, "name");
    	service.execute();
    	
    }
    
    //short username case
    @Test(expected = InvalidUsernameException.class)
    public void usernameTooShort(){
    	CreateUserIntegrator service = new CreateUserIntegrator(root_token, "a", EMAIL, "name");
    	service.execute();
    	
    }
    
    //user with no permission case
    @Test(expected = PermissionException.class)
    public void unauthorizedUserCreation() {
        CreateUserIntegrator service = new CreateUserIntegrator(user_token, USERNAME, "queijo",
                "Sensei");
        service.execute();
        
    }
    
    //root not in session case
    @Test(expected = UserNotInSessionException.class)
    public void NotInSession() {
        removeUserFromSession(root_token);
        CreateUserIntegrator service = new CreateUserIntegrator(root_token, USERNAME, "queijo",
                "Sensei");
        service.execute();
        
    }
    
    //duplicate username case
    @Test(expected = DuplicateEmailException.class)
    public void DuplicateEmail(){
    	CreateUserIntegrator service = new CreateUserIntegrator(root_token, USERNAME, EMAIL, NAME);
    	service.execute();
    }

    //invalid email case
    @Test(expected = InvalidEmailException.class)
    public void InvalidEmail(){
    	CreateUserIntegrator service = new CreateUserIntegrator(root_token, USERNAME, "im no mail", NAME);
    	service.execute();

    }
    
    //remote service exception case
    @Test(expected = UnavailableServiceException.class)
	public void unavailable() {
    	new MockSDIdUnavailable();
		CreateUserIntegrator service = new CreateUserIntegrator(root_token, USERNAME, EMAIL, NAME);
		service.execute();
	}
    


}
