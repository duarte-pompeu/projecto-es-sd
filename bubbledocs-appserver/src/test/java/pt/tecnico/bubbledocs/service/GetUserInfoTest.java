
package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;




public class GetUserInfoTest extends BubbleDocsServiceTest {

	
    // the tokens
    private String root_token;
    private String user_token;

    private static final String BAD_USERNAME = "IM A BIG BAD USER";;
	private static final String EMAIL_PRESENT = "Celtics@playoffs.com";
	private static final String USERNAME_PRESENT = "rato";
    private static final String NAME_PRESENT = "Franklin" ; 
    private static final String ROOT_USERNAME = "root";


    @Override
    public void populate4Test() {
    	root_token = addUserToSession(ROOT_USERNAME);
    	createUser(USERNAME_PRESENT, EMAIL_PRESENT, null, NAME_PRESENT);
        user_token = addUserToSession(USERNAME_PRESENT);
    }

    @Test
    public void success() {
    	GetUserInfo service = new GetUserInfo(USERNAME_PRESENT);
        service.execute();

        // User is the domain class that represents a User
        User user = service.getResult();
      
        //only this 3 fields are recuperated by User integrators
        assertEquals("wrong username",USERNAME_PRESENT, user.getUserName());
        assertEquals("wrong email",EMAIL_PRESENT, user.getEmail());
        assertEquals("wrong name", NAME_PRESENT,user.getName());
    }


    //TODO: empty username: InvalidValueException or InvalidUserNameException ???
    @Test(expected = InvalidUsernameException.class)
    public void emptyUsername() {
    	GetUserInfo service = new GetUserInfo("");
        service.execute();
    }
    
    //TODO: empty username: InvalidValueException or InvalidUserNameException ???
    @Test(expected = InvalidUsernameException.class)
    public void nullUsername() {
    	GetUserInfo service = new GetUserInfo(null);
        service.execute();
    }

    @Test(expected = NotFoundException.class)
    public void inexistentUsername() {
    	GetUserInfo service = new GetUserInfo(BAD_USERNAME);
        service.execute();
    }
    
    @Test(expected = NotFoundException.class)
    public void getInfoAfterRemove() {
    	removeUser(USERNAME_PRESENT);
    	GetUserInfo service = new GetUserInfo(USERNAME_PRESENT);
        service.execute();
    }
	
}
