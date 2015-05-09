package pt.tecnico.bubbledocs.integration.system;

import static org.junit.Assert.assertEquals;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import mockit.Verifications;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.integration.LoginUserIntegrator;

public class RemoteSystemIT {

	static BubbleDocs bd;
	
	 private String root_token;
	 private String user_token;
	 private CalcSheet created_spread;
	 private Reference reference;
	 private static final String ROOT_USERNAME = "root";
	 private static final String ROOT_PASSWORD = "rootroot";
	
	 @Before
	    public void setUp() throws Exception {
	    	   try {
	               FenixFramework.getTransactionManager().begin(false);
	               bd = BubbleDocs.getInstance();
	           } catch (WriteOnReadError | NotSupportedException | SystemException e1) {
	               e1.printStackTrace();
	           }
	       }

	    @After
	    public void tearDown() {
	    	
	    	try {
	    		bd.deleteBubbleDocs();
	            FenixFramework.getTransactionManager().commit();
	        } catch (IllegalStateException | SecurityException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    @Test
		public void doSequence() throws Exception {
	    	
				//root starts session
	    		LoginUserIntegrator loginRoot = new LoginUserIntegrator(ROOT_USERNAME,ROOT_PASSWORD);
	    		loginRoot.execute();
	    		root_token = loginRoot.getResult();
	    		
	    		User login_root = bd.getSessionFromToken(root_token).getUser();
	    		assertEquals("Usernames don't match", ROOT_USERNAME, login_root.getUserName());
	    		assertEquals("Password don't match", ROOT_PASSWORD, login_root.getPassword());
	    		assertEquals("Token doesn't match", root_token, login_root.getSession().getToken());
	
	    }
}
