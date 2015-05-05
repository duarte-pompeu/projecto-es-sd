package pt.tecnico.bubbledocs.integration.system;

import static org.junit.Assert.assertEquals;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import mockit.Mocked;
import mockit.Verifications;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;






import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.integration.CreateSpreadSheetIntegrator;
import pt.tecnico.bubbledocs.integration.CreateUserIntegrator;
import pt.tecnico.bubbledocs.integration.LoginUserIntegrator;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;

public class LocalSystemTest {
	
		@Mocked
		IDRemoteServices remoteID;
		@Mocked
		StoreRemoteServices remoteSTORE;
		static BubbleDocs bd;
		
		 private String root_token;
		 private String user_token;
		 private static final String ROOT_USERNAME = "root";
		 private static final String ROOT_PASSWORD = "rootroot";
		 private static final String USERNAME = "jakim";
		 private static final String EMAIL = "joaquim@xirabaita.com";
		 private static final String NAME = "Joaquim";
		 private static final String CALCSHEET_NAME = "Sinonimos de Joaquim";
		 private static final int CALCSHEET_ROWS = 50;
		 private static final int CALCSHEET_COLUMNS = 50;
		
		 
		
		 
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
	    	
	    	//integrators go here
	    	
				//root starts session
	    		LoginUserIntegrator loginRoot = new LoginUserIntegrator(ROOT_USERNAME,ROOT_PASSWORD);
	    		loginRoot.execute();
	    		new Verifications() {{
	    			remoteID.loginUser(ROOT_USERNAME,ROOT_PASSWORD); times = 1;
	    		}};
	    		root_token = loginRoot.getResult();
	    		
	    		User login_root = bd.getSessionFromToken(root_token).getUser();
	    		assertEquals("Usernames don't match", ROOT_USERNAME, login_root.getUserName());
	    		assertEquals("Password don't match", ROOT_PASSWORD, login_root.getPassword());
	    		assertEquals("Token doesn't match", root_token, login_root.getSession().getToken());
	    		
	    		
	    		//creates an user
	    		CreateUserIntegrator user = new CreateUserIntegrator(root_token,USERNAME,EMAIL,NAME);
	    		user.execute();
	    		new Verifications() {{
	    			remoteID.createUser(USERNAME,EMAIL); times = 1;
	    		}};
	    		
	    		  User check_user = bd.getUser(USERNAME);
	    		  assertEquals("Usernames don't match",USERNAME, check_user.getUserName());
	    		  assertEquals("Usernames don't match",EMAIL, check_user.getEmail());
	    		  assertEquals("Usernames don't match",NAME, check_user.getName());
	    		
	    		//the user starts a session on the app
	    		LoginUserIntegrator loginUser = new LoginUserIntegrator(USERNAME,"some random password");
	    		loginUser.execute();
	    		new Verifications() {{
	    			remoteID.loginUser(USERNAME,"some random password"); times = 1;
	    		}};
	    		user_token = loginUser.getResult();
	    		
	    		User login_user = bd.getSessionFromToken(user_token).getUser();
	    		assertEquals("Usernames don't match", USERNAME, login_user.getUserName());
	    		assertEquals("Password don't match", "some random password", login_user.getPassword());
	    		assertEquals("Token doesn't match", user_token, login_user.getSession().getToken());
	    		
	    		//user has fun
	    		CreateSpreadSheetIntegrator spread = new CreateSpreadSheetIntegrator(user_token,CALCSHEET_NAME,CALCSHEET_ROWS,CALCSHEET_COLUMNS);
	    		
	    }
	    	
	
}

