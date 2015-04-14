package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertTrue;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;


public class DeleteUserTest extends BubbleDocsServiceTest {

	
	@Mocked
	IDRemoteServices remote;
	
    private static final String USERNAME_TO_DELETE = "todelete";
	
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
        toDelete = createUser(USERNAME_TO_DELETE, "me@example.lol", "please", "john de lete");
        createSpreadSheet(toDelete, USERNAME_TO_DELETE, 20, 20);
        
       User rootUser = createUser(ROOT_USERNAME,"root@bigboss.com", "root", "root");
        BubbleDocs.getInstance().addUser(rootUser);
        root = addUserToSession(ROOT_USERNAME);
    };

    public void success() {
        DeleteUser service = new DeleteUser(root, USERNAME_TO_DELETE);
        service.execute();

        boolean caught = false;
        try {
        	this.getUserFromUsername(USERNAME_TO_DELETE);
        } catch (NotFoundException e) {
        	caught = true; //cool
        }        
        
        assertTrue("User should not exist", caught);
        
        caught = false;
        try {
        	this.getUserFromUsername(USERNAME_TO_DELETE);
        } catch (NotFoundException e) {
        	caught = true; //cool
        }
        
        assertTrue("Spreadsheet should not exist", caught);

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
        new DeleteUser(root, ALT_USERNAME).execute();
    }

    @Test(expected = PermissionException.class)
    public void notRootUser() {
        String user_token = addUserToSession(USERNAME);
        new DeleteUser(user_token, USERNAME_TO_DELETE).execute();
    }

    @Test(expected = UserNotInSessionException.class)
    public void rootNotInSession() {
        removeUserFromSession(root);

        new DeleteUser(root, USERNAME_TO_DELETE).execute();
    }

    @Test(expected = UserNotInSessionException.class)
    public void notInSessionAndNotRoot() {
        String user_token = addUserToSession(USERNAME);
        removeUserFromSession(user_token);

        new DeleteUser(user_token, USERNAME_TO_DELETE).execute();
    }

    @Test(expected = UserNotInSessionException.class)
    public void accessUserDoesNotExist() {
        new DeleteUser(ALT_USERNAME, USERNAME_TO_DELETE).execute();
    }
    
    @Test(expected = UnavailableServiceException.class)
	public void unavailable() {
		new Expectations() {{
			remote.removeUser(USERNAME_TO_DELETE); times = 1;
			result = new RemoteInvocationException();
		}};
		
		DeleteUser service = new DeleteUser(root, USERNAME_TO_DELETE);
		service.execute();
	}
}
