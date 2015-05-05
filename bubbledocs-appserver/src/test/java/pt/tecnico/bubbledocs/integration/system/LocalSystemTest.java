package pt.tecnico.bubbledocs.integration.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.integration.AssignLiteralCellIntegrator;
import pt.tecnico.bubbledocs.integration.CreateSpreadSheetIntegrator;
import pt.tecnico.bubbledocs.integration.CreateUserIntegrator;
import pt.tecnico.bubbledocs.integration.LoginUserIntegrator;
import pt.tecnico.bubbledocs.service.AssignLiteralCell;
import pt.tecnico.bubbledocs.service.AssignReferenceCell;
import pt.tecnico.bubbledocs.service.GetSpreadsheetContentService;
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
		 private CalcSheet created_spread;
		 private Reference reference;
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
	    
	    
	    /* global test that uses all integrators by sequence,
	     * after each integrator is executed, 
	     * we verify if the database is updated with the new objects
	     */
	    
	    @Test
		public void doSequence() throws Exception {
	    	
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
	    		
	    		//user creates empty calcsheet
	    		CreateSpreadSheetIntegrator spread = new CreateSpreadSheetIntegrator(user_token,CALCSHEET_NAME,CALCSHEET_ROWS,CALCSHEET_COLUMNS);
	    		spread.execute();
	    		created_spread = spread.getResult();
	    		int CALCSHEET_ID=created_spread.getId();
	    		
	    		CalcSheet calc = bd.getCalcSheetByName(created_spread.getName());
	    		assertNotNull("calcsheet wasnt created", calc);
	    		assertEquals("Invalid owner", USERNAME, calc.getCreator().getUserName());
	    		assertEquals("Invalid calcsheet name", CALCSHEET_NAME, calc.getName());
	    		assertEquals("Invalid calcsheet rows", CALCSHEET_COLUMNS, (int) calc.getColumns());
	    		assertEquals("Invalid calcsheet lines", CALCSHEET_ROWS, (int) calc.getLines());
	    				
	    		//assert all the cells
	    		for(int c = 1; c <= CALCSHEET_COLUMNS; c++){
	    			for(int l = 1; l <= CALCSHEET_ROWS; l++){
	    				Cell cell = calc.getCell(l,c);
	    				assertEquals(new Integer(c), new Integer(cell.getColumn()));
	    				assertEquals(new Integer(l), new Integer(cell.getLine()));
	    			}
	    		}
	    		
	    		//time 2 fill up my new calcsheet
	    		
	    		AssignLiteralCellIntegrator alc = new AssignLiteralCellIntegrator(user_token, CALCSHEET_ID, created_spread.getCell(1, 1).getId(), "5");
	    		alc.execute();
	    		reference = new Reference(created_spread.getCell(1,1));
	    		AssignReferenceCell arc = new AssignReferenceCell(user_token, CALCSHEET_ID, created_spread.getCell(2, 2).getId(), "1;1");
	    		arc.execute();
	    		
	    		
	    }
	    	
	
}

