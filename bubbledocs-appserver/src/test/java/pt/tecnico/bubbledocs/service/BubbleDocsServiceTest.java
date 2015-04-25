package pt.tecnico.bubbledocs.service;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.Session;
import pt.tecnico.bubbledocs.domain.User;

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
    // for defining the inital state and checking that the service has the expected behavior
    @Deprecated
    User createUser(String username, String password, String name) {
    	return createUser(username, null, password, name);
    }
    
    User createUser(String username, String email, String password, String name) {
    	return BubbleDocs.getInstance().addTestUser(username, name, email, password);
    }
    
    void removeUser(String username) {
    	BubbleDocs.getInstance().removeTestUser(username);
    }

    public CalcSheet createSpreadSheet(User user, String name, int row,
            int column) {
		BubbleDocs bd = BubbleDocs.getInstance();
		CalcSheet cs = user.createCalcSheet(name, row, column);
		bd.addCalcSheet(cs);
    	
		return cs;
    }

    // returns a spreadsheet whose name is equal to name
    public CalcSheet getSpreadSheet(String name) {
		BubbleDocs bd = BubbleDocs.getInstance();
		return bd.getCalcSheetByName(name);
    }

    // returns the user registered in the application whose username is equal to username
    User getUserFromUsername(String username) {
    	return BubbleDocs.getInstance().getUser(username);
    }

    // put a user into session and returns the token associated to it
    String addUserToSession(String username) {
    	User user = BubbleDocs.getInstance().getUser(username);
    	return BubbleDocs.getInstance().addSession(user);
    }

    // remove a user from session given its token
    void removeUserFromSession(String token) {
    	Session session = getSessionFromToken(token);
    	DateTime time = new DateTime();
    	time = time.minusHours(1);
    	session.setExpiration(time);
    }

    // return the user registered in session whose token is equal to token
    User getUserFromSession(String token) {
    	return getSessionFromToken(token).getUser();
    }

    Session getSessionFromToken(String token) {
    	return BubbleDocs.getInstance().getSessionFromToken(token);
    }

}
