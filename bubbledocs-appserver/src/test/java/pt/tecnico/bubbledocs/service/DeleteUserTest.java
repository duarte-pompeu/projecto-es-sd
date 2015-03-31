package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.SuperUser;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;


public class DeleteUserTest extends BubbleDocsServiceTest {

    private static final String USERNAME_TO_DELETE = "todelete";
	
    private static final String USERNAME = "Tartaruga";
    private static final String PASSWORD = "pizza";
	private static final String NAME = "Franklin"; 
    private static final String ROOT_USERNAME = "root";
	
    private static final String ALT_USERNAME = "rato";
	
    private static final String SPREADSHEET_NAME = "spread";

	private User toDelete;
    // the tokens for user root
    private String root;

    @Override
    public void populate4Test() {
        createUser(USERNAME, PASSWORD, NAME);
        toDelete = createUser(USERNAME_TO_DELETE, "please", "john de lete");
        createSpreadSheet(toDelete, USERNAME_TO_DELETE, 20, 20);
        
       User rootUser = createUser(ROOT_USERNAME, "root", "root");
        BubbleDocs.getInstance().addUser(rootUser);
        root = addUserToSession(ROOT_USERNAME);
    };

    public void success() {
        DeleteUser service = new DeleteUser(root, USERNAME_TO_DELETE);
        service.execute();

        boolean deleted = (getUserFromUsername(USERNAME_TO_DELETE) == null);

        assertTrue("user was not deleted", deleted);

        assertNull("Spreadsheet was not deleted",
                getSpreadSheet(SPREADSHEET_NAME));
    }

	
    @Test
    public void successToDeleteIsNotInSession() {
        success();
    }

    @Test
    public void successToDeleteIsInSession() {
        String token = addUserToSession(USERNAME_TO_DELETE);
        success();
	assertNull("User removed but not from session", getUserFromSession(token));
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
}
