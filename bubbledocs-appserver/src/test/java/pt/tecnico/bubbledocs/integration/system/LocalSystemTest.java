package pt.tecnico.bubbledocs.integration.system;

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


import pt.tecnico.bubbledocs.domain.BubbleDocs;
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
		 private static final String USERNAME = "jakim";
		 private static final String EMAIL = "joaquim@xirabaita.com";
		 private static final String NAME = "Joaquim";
		 private static final String CALCSHEET_NAME = "Sinonimos de Joaquim";
		 private static final int CALCSHEET_ROWS = 50;
		 private static final int CALCSHEET_COLUMNS = 50;
		
		 
		
		 
	    @Before
	    public void setUp() throws Exception {
	    	bd = BubbleDocs.getInstance();
	    }

	    @After
	    public void tearDown() {
	    	bd.deleteBubbleDocs();
	    }
	    
	    @Test
		public void doSequence() throws Exception {
	    	
	    	//integrators go here
	    	
				//root starts session
	    		LoginUserIntegrator loginRoot = new LoginUserIntegrator("root","rootroot");
	    		loginRoot.execute();
	    		new Verifications() {{
	    			remoteID.loginUser("root","rootroot"); times = 1;
	    		}};
	    		root_token = loginRoot.getResult();
	    		
	    		//creates an user
	    		CreateUserIntegrator user = new CreateUserIntegrator(root_token,USERNAME,EMAIL,NAME);
	    		user.execute();
	    		new Verifications() {{
	    			remoteID.createUser(USERNAME,EMAIL); times = 1;
	    		}};
	    		
	    		
	    		//the user starts a session on the app
	    		LoginUserIntegrator loginUser = new LoginUserIntegrator(USERNAME,"some random password");
	    		loginUser.execute();
	    		new Verifications() {{
	    			remoteID.loginUser(USERNAME,"some random password"); times = 1;
	    		}};
	    		user_token = loginUser.getResult();
	    		
	    		//user has fun
	    		CreateSpreadSheetIntegrator spread = new CreateSpreadSheetIntegrator(user_token,CALCSHEET_NAME,CALCSHEET_ROWS,CALCSHEET_COLUMNS);
	    		
	    }
	    	
	
}

