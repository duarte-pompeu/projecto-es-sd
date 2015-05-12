package pt.tecnico.bubbledocs.integration.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Content;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.Session;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.integration.AssignBinaryToCellIntegrator;
import pt.tecnico.bubbledocs.integration.AssignLiteralCellIntegrator;
import pt.tecnico.bubbledocs.integration.CreateSpreadSheetIntegrator;
import pt.tecnico.bubbledocs.integration.CreateUserIntegrator;
import pt.tecnico.bubbledocs.integration.DeleteUserIntegrator;
import pt.tecnico.bubbledocs.integration.ExportDocumentIntegrator;
import pt.tecnico.bubbledocs.integration.GetSpreadsheetContentIntegrator;
import pt.tecnico.bubbledocs.integration.ImportDocumentIntegrator;
import pt.tecnico.bubbledocs.integration.LoginUserIntegrator;
import pt.tecnico.bubbledocs.integration.RenewPasswordIntegrator;
import pt.tecnico.bubbledocs.service.AssignReferenceCell;
import pt.tecnico.bubbledocs.service.GetUserInfo;
import pt.tecnico.bubbledocs.service.GetUsername4Token;
public class RemoteSystemIT {

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
	 private static final String add_Expression =  "=ADD(2,1;1)";
	 private static final String div_Expression =  "=DIV(1;1,2;2)";
	 
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
	    		
	    		//creates an user
	    		CreateUserIntegrator user = new CreateUserIntegrator(root_token,USERNAME,EMAIL,NAME);
	    		user.execute();
	    		
	    		  GetUserInfo UserInfo = new GetUserInfo(USERNAME);
	    		  UserInfo.execute();
	    		  User check_user = UserInfo.getResult();
	    		  
	    		  assertEquals("Usernames don't match",USERNAME, check_user.getUserName());
	    		  assertEquals("Usernames don't match",EMAIL, check_user.getEmail());
	    		  assertEquals("Usernames don't match",NAME, check_user.getName());
	    		
	    		  
	    			//the user starts a session on the app
		    		LoginUserIntegrator loginUser = new LoginUserIntegrator(USERNAME,"some random password");
		    		loginUser.execute();
		    		user_token = loginUser.getResult();
		    		
		    		GetUsername4Token Username4token = new GetUsername4Token(user_token);
		    		Username4token.execute();
		    		String username4token = Username4token.getResult();
		    		Session session = bd.getSessionFromToken(user_token);
		 
		    		GetUserInfo UserInfo3 = new GetUserInfo(USERNAME);
		    		UserInfo3.execute();
		    		User after_login = UserInfo3.getResult();
		   
		    	
		    		assertEquals("Usernames don't match", USERNAME, username4token);
		    		assertEquals("Password don't match", "some random password", after_login.getPassword());
		    		assertEquals("Token doesn't match", user_token, session.getToken());
		    		
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
		    		String lit_result = alc.getResult();
		    		String cell_id=created_spread.getCell(1, 1).getId();
		    		String cell_id2=created_spread.getCell(2, 2).getId();
		    		String cell_id3=created_spread.getCell(4, 4).getId();
		    		
		    		assertEquals("Value is NOT correct", 5, calc.getCell(cell_id).getContent().getValue());
		    		assertEquals("Bad result",calc.getCell(cell_id).getContent().toString(),lit_result);
		    		
		    		
		    		reference = new Reference(created_spread.getCell(1,1));
		    		AssignReferenceCell arc = new AssignReferenceCell(user_token, CALCSHEET_ID, created_spread.getCell(3, 3).getId(), "1;1");
		    		arc.execute();
		    		String ref_result = arc.getResult();
		    		String ref_id = calc.getCell(3, 3).getId();
		    		Reference comp_ref = new Reference (calc.getCell(ref_id));
		    		
		    		assertEquals("Reference is NOT correct", ref_id, comp_ref.getPointedCell().getId());
		    		assertEquals("Bad result", "=1;1", ref_result);
		    		
		    		
		    		
		    		AssignBinaryToCellIntegrator add = new AssignBinaryToCellIntegrator( cell_id2, add_Expression ,CALCSHEET_ID, user_token);
		    		add.execute();

		    		assertEquals("Content value is incorrect", add.getResult(), "7");
		    		
		    		
		    		AssignBinaryToCellIntegrator div = new AssignBinaryToCellIntegrator( cell_id3, div_Expression ,CALCSHEET_ID, user_token);
		    		div.execute();

		    		assertEquals("Content value is incorrect", div.getResult(), "0");
		    		
		    		//time 2 renew my password
		    		
		    		RenewPasswordIntegrator renewPass = new RenewPasswordIntegrator(user_token);
		    		renewPass.execute();
		    		
		    		GetUserInfo UserInfo2 = new GetUserInfo(USERNAME);
		    		UserInfo2.execute();
		    		User after_user = UserInfo2.getResult();
		    		assertNull("password should be null",after_user.getPassword());
		    		
		    		//get content
		    		GetSpreadsheetContentIntegrator gscs = new GetSpreadsheetContentIntegrator(user_token, Integer.toString(CALCSHEET_ID));
		    		gscs.execute();
		    		String [][] content = gscs.getResult();
		    		
		    		assertEquals(content[0].length, CALCSHEET_COLUMNS);
		    		assertEquals(content.length, CALCSHEET_ROWS);
		    		assertEquals("5", content[0][0]);
		    		assertEquals(reference.toString(), content[1][1]);
		    		
		    		
		    		
		    		ExportDocumentIntegrator export = new ExportDocumentIntegrator(user_token, CALCSHEET_ID);
		    		export.execute();
		    		
		    		
		    		ImportDocumentIntegrator imported = new ImportDocumentIntegrator(user_token, CALCSHEET_ID);
		    		imported.execute();
		    		

		    		assertNotEquals("old id and new id are the same", imported.getOldDocId(), imported.getNewDocId());
		    		
		    		CalcSheet oldSheet = calc;
		    		
		    		//O novo CalcSheet tem de existir.
		    		CalcSheet newSheet = BubbleDocs.getInstance().getCalcSheetById(imported.getNewDocId());
		    		
		    		assertEquals("creators are different", oldSheet.getCreator().getUserName(), newSheet.getCreator().getUserName());
		    		assertEquals("names are different", oldSheet.getName(), newSheet.getName());
		    		assertEquals("dates are different", oldSheet.getDate(), newSheet.getDate());
		    		assertEquals("lines are different", oldSheet.getLines(), newSheet.getLines());
		    		assertEquals("columns are different", oldSheet.getColumns(), newSheet.getLines());
		    		
		    		Content newContent = newSheet.getCell(cell_id).getContent();
		    		assertTrue("it's not a Literal", newContent instanceof Literal);
		    		assertEquals("value is not a literal 5", 5, newContent.getValue());
		    		
		    		 DeleteUserIntegrator service = new DeleteUserIntegrator(root_token, USERNAME);
		    	        service.execute();

		    	        
		    	        try {
		    	        	GetUserInfo UserInfo4 = new GetUserInfo(USERNAME);
		    	    		UserInfo4.execute();
		    	        	fail("User should not exist");
		    	        } catch (NotFoundException e) {
		    	        	//cool
		    	        } 
		    	        
		    	        try {
		    	        	bd.getCalcSheetByName(CALCSHEET_NAME);
		    	        	fail("Spreadsheet should not exist");
		    	        } catch (NotFoundException e) {
		    	        	//double cool
		    	        }   
	
	    }
}
