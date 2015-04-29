package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.*;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.integration.DeleteUserIntegrator;
import pt.tecnico.bubbledocs.service.BubbleDocsServiceTest;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;


public class DeleteUserIntegratorTest extends BubbleDocsServiceTest {

	
	@Mocked
	IDRemoteServices remote;	
    private static final String USERNAME_TO_DELETE = "todelete";
    private static final String SPREADSHEET_TO_DELETE = "Not a cheat sheet";
    private static final String USERNAME = "turtle";
    private static final String PASSWORD = "pizza";
    private static final String EMAIL = "quack@patos.com";
	private static final String NAME = "Franklin"; 
    private static final String ROOT_USERNAME = "root";	
    private static final String ALT_USERNAME = "rato";

	private User toDelete;
    // the tokens for user root
    private String root;

    @Override
    public void populate4Test() {
        createUser(USERNAME,EMAIL, PASSWORD, NAME);
        toDelete = createUser(USERNAME_TO_DELETE, "me@example.lol", "please", "John deLete");
        createSpreadSheet(toDelete, SPREADSHEET_TO_DELETE, 20, 20);

        root = addUserToSession(ROOT_USERNAME);
    };

    public void success() {
        DeleteUserIntegrator service = new DeleteUserIntegrator(root, USERNAME_TO_DELETE);
        service.execute();

        new Verifications() {{ //verify the service was called
			remote.removeUser(USERNAME_TO_DELETE); times = 1;
		}};
        
        try {
        	this.getUserFromUsername(USERNAME_TO_DELETE);
        	fail("User should not exist");
        } catch (NotFoundException e) {
        	//cool
        } 
        
        try {
        	this.getSpreadSheet(SPREADSHEET_TO_DELETE);
        	fail("Spreadsheet should not exist");
        } catch (NotFoundException e) {
        	//double cool
        }        
    }
    
    public void testCompensation() {
    	//The user that was supposed to be deleted
    	//Still exists;
    	User user = this.getUserFromUsername(USERNAME_TO_DELETE);
    	
    	//Some sanity asserts
    	assertEquals("Hum. Username is different?", user.getUserName(), USERNAME_TO_DELETE);
    	assertEquals("Name is different", user.getName(), "John deLete");
    	assertEquals("Email is different", user.getEmail(), "me@example.lol");
    }
	
    @Test
    public void successToDeleteIsNotInSession() {
        success();
    }

    @Test(expected = UserNotInSessionException.class)
    public void successToDeleteIsInSession() {
        String token = addUserToSession(USERNAME_TO_DELETE);
        success();
		getUserFromSession(token);
    }

    @Test(expected = NotFoundException.class)
    public void userToDeleteDoesNotExist() {
        new Expectations() {{
        	remote.removeUser(anyString); times = 0;
        }};

        new DeleteUserIntegrator(root, ALT_USERNAME).execute();
    }

    @Test(expected = PermissionException.class)
    public void notRootUser() {
        new Expectations() {{
        	remote.removeUser(anyString); times = 0;
        }};
    	
        String user_token = addUserToSession(USERNAME);
        new DeleteUserIntegrator(user_token, USERNAME_TO_DELETE).execute();
    }

    @Test(expected = UserNotInSessionException.class)
    public void rootNotInSession() {
        new Expectations() {{
        	remote.removeUser(anyString); times = 0;
        }};
    	
        removeUserFromSession(root);
        new DeleteUserIntegrator(root, USERNAME_TO_DELETE).execute();
    }
    
    @Test(expected = UserNotInSessionException.class)
	public void expired() {
		new Expectations() {{ 
			remote.removeUser(USERNAME_TO_DELETE); times = 0;
		}};
		
    	String user_token = addUserToSession(USERNAME);
        removeUserFromSession(user_token);
		DeleteUserIntegrator service = new DeleteUserIntegrator(user_token,USERNAME_TO_DELETE);
		service.execute();

	}
	
	@Test(expected = UserNotInSessionException.class)
	public void invalid() {
		new Expectations() {{ 
			remote.removeUser(USERNAME_TO_DELETE); times = 0;
		}};
		
		DeleteUserIntegrator service =  new DeleteUserIntegrator("i am a silly token", USERNAME_TO_DELETE);
		service.execute();
	}
    
    @Test
	public void unavailable() {
		new Expectations() {{
			remote.removeUser(USERNAME_TO_DELETE); times = 1;
			result = new RemoteInvocationException();
		}};
		
		DeleteUserIntegrator service = new DeleteUserIntegrator(root, USERNAME_TO_DELETE);
		try {
			service.execute();
			fail("expected UnavailableServiceException");
		} catch (UnavailableServiceException e) {
			testCompensation();
		}
	}
}
