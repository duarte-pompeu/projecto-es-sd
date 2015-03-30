package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.InvalidValueException;
import pt.tecnico.bubbledocs.exceptions.RepeatedIdentificationException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;


public class CreateUserTest extends BubbleDocsServiceTest {

    // the tokens
    private String root;
    private String user_token;

	private User USER;
    private static final String USERNAME = "tartaruga";
    private static final String PASSWORD = "pizza";
	private static final String NAME = "Franklin" ; 
    private static final String ROOT_USERNAME = "root";
    private static final String ALT_USERNAME = "rato";

    @Override
    public void populate4Test() {
		createUser(ROOT_USERNAME, PASSWORD, "Super User");
    	root = addUserToSession(ROOT_USERNAME);
        USER = createUser(USERNAME, PASSWORD, NAME);
        user_token = addUserToSession(USERNAME);
    }

    @Test
    public void success() {
        CreateUser service = new CreateUser(root, ALT_USERNAME, "queijo",
                "Sensei");
        service.execute();

	// User is the domain class that represents a User
        User user = getUserFromUsername(ALT_USERNAME);

        assertEquals(ALT_USERNAME, user.getUserName());
        assertEquals("queijo", user.getPassword());
        assertEquals("Sensei", user.getName());
    }

    @Test(expected =  RepeatedIdentificationException.class)
    public void usernameExists() {
        CreateUser service = new CreateUser(root, USERNAME, PASSWORD, NAME);
        service.execute();
    }

    @Test(expected = InvalidValueException.class)
    public void emptyUsername() {
        CreateUser service = new CreateUser(root, "", "queijo", "sensei");
        service.execute();
    }

    @Test(expected = PermissionException.class)
    public void unauthorizedUserCreation() {
        CreateUser service = new CreateUser(user_token, ALT_USERNAME, "queijo",
                "Sensei");
        service.execute();
    }

    @Test(expected = UserNotInSessionException.class)
    public void accessUsernameNotExist() {
        removeUserFromSession(root);
        CreateUser service = new CreateUser(root, ALT_USERNAME, "queijo",
                "Sensei");
        service.execute();
    }

}
