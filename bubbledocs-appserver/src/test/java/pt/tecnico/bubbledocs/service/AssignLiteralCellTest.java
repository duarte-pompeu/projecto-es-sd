package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.InvalidFormatException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;

public class AssignLiteralCellTest extends BubbleDocsServiceTest {
	
	private final String U_USERNAME = "jubileu";
	private final String U_PASS = "password";
	private final String U_NAME = "Jubileu Mandafacas";
	private final String U_MAIL = "jubi_m@nomail.com";
	private String U_TOKEN;
	
	private User USER;
	
	private CalcSheet CS_SHEET;
	private int CS_ID;
	private final String CS_NAME = "Cabulas ES";
	private final int CS_ROWS = 10;
	private final int CS_LINES = 10;
	
	private String CELL_ID0;
	private final int VAL0 = 1;
	private final String LIT0 = "1";

	
	@Override
	public void populate4Test(){
		USER = createUser(U_USERNAME, U_MAIL, U_PASS, U_NAME);
		U_TOKEN = addUserToSession(U_USERNAME);
		
		CS_SHEET = createSpreadSheet(USER, CS_NAME, CS_ROWS, CS_LINES);
		CS_ID = CS_SHEET.getId();
		CELL_ID0 = CS_SHEET.getCell(1, 1).getId();
	}
	
	
	@Test
	public void populateSuccess(){
		AssignLiteralCell service = new AssignLiteralCell(U_TOKEN, CS_ID, CELL_ID0, LIT0);
		service.execute();
		
		/* assertEquals indentation style might seem a little weird
		 * but it does make the tests more legible IMO
		 * -Duarte
		 */
		assertEquals("Owner is NOT correct",
				U_USERNAME, 
				getSpreadSheet(CS_NAME).getCreator().getUserName());
		
		assertEquals("User is NOT in session",
				U_USERNAME,
				getUserFromSession(U_TOKEN).getUserName());
		
		assertEquals("Cell ID is NOT correct",
				CELL_ID0, 
				CS_SHEET.getCell(CELL_ID0).getId());
		
		assertEquals("Value is NOT correct", 
				VAL0, 
				CS_SHEET.getCell(CELL_ID0).getContent().getValue());
		
		assertEquals("Bad result",
				CS_SHEET.getCell(CELL_ID0).getContent().toString(),
				service.getResult());
	}
	
	
	@Test
	public void insertInBoundary(){
		assertEquals("Line is NOT boundary line",
				new Integer(CS_LINES),
				CS_SHEET.getLines());
		assertEquals("Column is boundary column",
				new Integer(CS_ROWS),
				CS_SHEET.getColumns());
		
		String id = CS_LINES + ";" + CS_ROWS;
		
		AssignLiteralCell service = new AssignLiteralCell (U_TOKEN, CS_ID, id, LIT0);
		service.execute();
		
		assertEquals("Bad result",
				CS_SHEET.getCell(id).getContent().toString(),
				service.getResult());
	}
	
	
	@Test(expected = UserNotInSessionException.class)
	public void noLogin(){
		String bad_session_token = "Bad session token";
		AssignLiteralCell service = new AssignLiteralCell(bad_session_token, CS_ID, CELL_ID0, LIT0);
		service.execute();
	}
	
	
	@Test(expected = NotFoundException.class)
	public void docDoesntExist(){
		int bad_doc_id = -123;
		
		AssignLiteralCell service = new AssignLiteralCell(U_TOKEN, bad_doc_id, CELL_ID0, LIT0);
		service.execute();
	}

	
	@Test(expected = NotFoundException.class)
	public void cellDoesntExist(){
		String literal_str = "0";
		String bad_cell_id = "123";
		
		AssignLiteralCell service = new AssignLiteralCell (U_TOKEN, CS_ID, bad_cell_id, literal_str);
		service.execute();
	}
	
	
	@Test(expected = NotFoundException.class)
	public void cellOutsideBoundsLow(){
		String bad_cell_id = "0;0";
		
		AssignLiteralCell service = new AssignLiteralCell (U_TOKEN, CS_ID, bad_cell_id, LIT0);
		service.execute();
	}

	
	@Test(expected = NotFoundException.class)
	public void cellOutsideBoundsHigh(){
		int lines = 100;
		assertTrue("Position is NOT outside sheet boundaries",
				CS_SHEET.getLines() < lines);
		String bad_cell_id = lines + ";1";
		
		AssignLiteralCell service = new AssignLiteralCell (U_TOKEN, CS_ID, bad_cell_id, LIT0);
		service.execute();
	}
	
	
	@Test(expected = InvalidFormatException.class)
	public void literalHasDecimals(){
		String bad_literal = "1.5";
		AssignLiteralCell service = new AssignLiteralCell(U_TOKEN, CS_ID, CELL_ID0, bad_literal);
		service.execute();
	}
	
	
	@Test(expected = InvalidFormatException.class)
	public void literalIsText(){
		String bad_literal = "One hundred";
		AssignLiteralCell service = new AssignLiteralCell(U_TOKEN, CS_ID, CELL_ID0, bad_literal);
		service.execute();
	}
	
	@Test(expected = PermissionException.class)
	public void cellIsProtected(){
		Cell cell = CS_SHEET.getCell(CELL_ID0);
		assertNotNull(cell);
		
		//FIXME: this doesnt seem a proper way to change cell protection
		cell.setProtect(true);
		
		AssignLiteralCell service = new AssignLiteralCell(U_TOKEN, CS_ID, CELL_ID0, LIT0);
		service.execute();
	}
	
	
	@Test(expected = PermissionException.class)
	public void noWriteAccess(){
		User data_spy = createUser("NSA", "spy@nsa.gov", "password", "National Security Agency");
		String spy_token = addUserToSession(data_spy.getUserName());
		
		AssignLiteralCell service = new AssignLiteralCell(spy_token, CS_ID, CELL_ID0, LIT0);
		service.execute();
	}
}
