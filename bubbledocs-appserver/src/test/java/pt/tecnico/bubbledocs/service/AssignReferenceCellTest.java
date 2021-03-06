package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;

public class AssignReferenceCellTest extends BubbleDocsServiceTest {
	private final String U_USERNAME = "Maicou";
	private final String U_PASS = "password";
	private final String U_NAME = "Maicou Feupes";
	private final String U_MAIL = "maicou_feupes@suimepule.com";
	private String U_TOKEN;
	
	private User USER;
	
	private CalcSheet CS_SHEET;
	private int CS_ID;
	private final String CS_NAME = "Teste Referencia";
	private final int CS_ROWS = 10;
	private final int CS_LINES = 11;
	
	private String CELL_ID0;
	private String REFF_ID0;
	
	@Override
	public void populate4Test(){
		USER = createUser(U_USERNAME, U_MAIL, U_PASS, U_NAME);
		U_TOKEN = addUserToSession(U_USERNAME);
		
		CS_SHEET = createSpreadSheet(USER, CS_NAME, CS_ROWS, CS_LINES);
		CS_ID = CS_SHEET.getId();
		CELL_ID0 = CS_SHEET.getCell(1, 1).getId();
		REFF_ID0 = CS_SHEET.getCell(2, 2).getId();
		CS_SHEET.getCell(3,3).getId();
		Literal num1 = new Literal(5);
		CS_SHEET.setContent(USER, num1, REFF_ID0);
	}
	
	@Test
	public void populateSuccess(){
		AssignReferenceCell service = new AssignReferenceCell(U_TOKEN, CS_ID, CELL_ID0, REFF_ID0);
		service.execute();
		
		assertEquals("Owner is NOT correct",
				U_USERNAME, 
				getSpreadSheet(CS_NAME).getCreator().getUserName());
		assertEquals("User is NOT in session",
				U_USERNAME,
				getUserFromSession(U_TOKEN).getUserName());
		assertEquals("Cell ID is NOT correct",
				CELL_ID0, 
				CS_SHEET.getCell(CELL_ID0).getId());
		Reference comp_ref = new Reference (CS_SHEET.getCell(REFF_ID0));
		assertEquals("Reference is NOT correct", 
				REFF_ID0, 
				comp_ref.getPointedCell().getId());
		assertEquals("Bad result", 
				"=2;2", 
				service.getResult());
	}
	
	@Test(expected = UserNotInSessionException.class)
	public void NoSession(){
		String bad_token = "RandomToken";
		AssignReferenceCell service = new AssignReferenceCell(bad_token, CS_ID, CELL_ID0, REFF_ID0);
		service.execute();
	}
	
	@Test(expected = NotFoundException.class)
	public void DocDoesntExist(){
		int bad_cs_id = -9000; 
		AssignReferenceCell service = new AssignReferenceCell(U_TOKEN, bad_cs_id, CELL_ID0, REFF_ID0);
		service.execute();
	}
	
	@Test(expected = NotFoundException.class)
	public void CellDoesntExist(){
		String bad_cell_id = "666";
		
		AssignReferenceCell service = new AssignReferenceCell (U_TOKEN, CS_ID, bad_cell_id, REFF_ID0);
		service.execute();
	}
	
	@Test (expected = NotFoundException.class)
	public void outOfBounds1(){
		String cellID = "0;0";
		
		AssignReferenceCell service = new AssignReferenceCell (U_TOKEN, CS_ID, cellID, REFF_ID0);
		service.execute();
	}
	
	
	@Test (expected = NotFoundException.class)
	public void outOfBounds2(){
		String cellID = Integer.toString(CS_LINES) + ";" + Integer.toString(CS_ROWS);
		
		AssignReferenceCell service = new AssignReferenceCell (U_TOKEN, CS_ID, cellID, REFF_ID0);
		service.execute();
	}
	
	
	@Test(expected = NotFoundException.class)
	public void BadReference() {
		String bad_reference_id = "999";
		
		AssignReferenceCell service = new AssignReferenceCell (U_TOKEN, CS_ID, CELL_ID0, bad_reference_id);
		service.execute();
	}
	
	@Test(expected = PermissionException.class)
	public void NoPermissions(){
		User imqt = createUser("imqtpie", "qtpie@example.com", "easypass", "Cutie Pie");
		String new_token = addUserToSession(imqt.getUserName());
		
		AssignReferenceCell service = new AssignReferenceCell(new_token, CS_ID, CELL_ID0, REFF_ID0);
		service.execute();
	}
}
	
	