package pt.tecnico.bubbledocs.service;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.After;
import org.junit.Before;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.tecnico.bubbledocs.dml.*;

// add needed import declarations

public class BubbleDocsServiceTest {

    @Before
    public void setUp() throws Exception {

        try {
            FenixFramework.getTransactionManager().begin(false);
            populate4Test();
        } catch (WriteOnReadError | NotSupportedException | SystemException e1) {
            e1.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        try {
            FenixFramework.getTransactionManager().rollback();
        } catch (IllegalStateException | SecurityException | SystemException e) {
            e.printStackTrace();
        }
    }

    // should redefine this method in the subclasses if it is needed to specify
    // some initial state
    public void populate4Test() {
    }

    // auxiliary methods that access the domain layer and are needed in the test classes
    // for defining the iniital state and checking that the service has the expected behavior
    User createUser(String username, String password, String name) {
    	return BubbleDocs.getInstance().addUser(username, name, password);
    }

    public CalcSheet createSpreadSheet(User user, String name, int row,
            int column) {
	// add code here
    	return null;
    }

    // returns a spreadsheet whose name is equal to name
    public CalcSheet getSpreadSheet(String name) {
	// add code here
    	return null;
    }

    // returns the user registered in the application whose username is equal to username
    User getUserFromUsername(String username) {
	// add code here
    	return BubbleDocs.getInstance().getUser(username);
    }

    // put a user into session and returns the token associated to it
    String addUserToSession(String username) {
    	User user = BubbleDocs.getInstance().getUser(username);
    	return BubbleDocs.getInstance().addSession(user);
    }

    // remove a user from session given its token
    void removeUserFromSession(String token) {
	// add code here
    }

    // return the user registered in session whose token is equal to token
    User getUserFromSession(String token) {
    	return getSessionFromToken(token).getUser();
    }

    Session getSessionFromToken(String token) {
    	return BubbleDocs.getInstance().getSessionFromToken(token);
    }

}
