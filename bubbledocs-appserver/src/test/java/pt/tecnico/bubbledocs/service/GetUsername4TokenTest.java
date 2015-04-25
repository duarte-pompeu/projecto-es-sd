
package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


import org.junit.Test;

import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;




public class GetUsername4TokenTest extends BubbleDocsServiceTest {

	
    // the tokens
    private String root_token;
    private String user_token;
    
    
    private static final String BAD_TOKEN = "do you even token?";;
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
    	GetUsername4Token service = new GetUsername4Token(user_token);
        service.execute();
        
        GetUsername4Token service2 = new GetUsername4Token(root_token);
        service2.execute();

        //return usernames
        String userName = service.getResult();
        String rootUserName = service2.getResult();
      
        //check if all good
        assertEquals("wrong username",USERNAME_PRESENT, userName);
        assertNotNull(userName);
        
        assertEquals("wrong root username",ROOT_USERNAME, rootUserName);
        assertNotNull(rootUserName);
    }

    
    @Test(expected = UserNotInSessionException.class)
    public void emptyToken() {
    	GetUsername4Token service = new GetUsername4Token("");
        service.execute();
    }
    

    @Test(expected = UserNotInSessionException.class)
    public void nullToken() {
    	GetUsername4Token service = new GetUsername4Token(null);
        service.execute();
    }

    @Test(expected = UserNotInSessionException.class)
    public void inexistentToken() {
    	GetUsername4Token service = new GetUsername4Token(BAD_TOKEN);
        service.execute();
    }
    
    @Test(expected = UserNotInSessionException.class)
	public void expiredUser() {
		removeUserFromSession(user_token);
		GetUsername4Token service = new GetUsername4Token(user_token);
        service.execute();
	}
    
    @Test(expected = UserNotInSessionException.class)
	public void expiredRoot() {
		removeUserFromSession(root_token);
		GetUsername4Token service = new GetUsername4Token(root_token);
        service.execute();
	}
	
}
