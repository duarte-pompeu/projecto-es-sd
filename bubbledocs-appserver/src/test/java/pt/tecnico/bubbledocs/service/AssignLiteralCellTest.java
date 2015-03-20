package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.tecnico.bubbledocs.dml.CalcSheet;
import pt.tecnico.bubbledocs.dml.Cell;
import pt.tecnico.bubbledocs.dml.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;

public class AssignLiteralCellTest extends BubbleDocsServiceTest {
	
	private final String U_USERNAME = "jubileu";
	private final String U_PASS = "password";
	private final String U_NAME = "Jubileu Mandafacas";
	
	private User USER;
	
	private CalcSheet CS_SHEET;
	private int CS_ID;
	private final String CS_NAME = "Cabulas ES";
	private final int CS_ROWS = 10;
	private final int CS_LINES = 10;
	
	private String CELL_ID0;
	private final int VAL0 = 0;
	private final String LIT0 = "0";

	
	@Override
	public void populate4Test(){
		USER = createUser(U_USERNAME, U_PASS, U_NAME);
		addUserToSession(U_USERNAME);
		
		CS_SHEET = createSpreadSheet(USER, CS_NAME, CS_ROWS, CS_LINES);
		CS_ID = CS_SHEET.getId();
		CELL_ID0 = CS_SHEET.getCell(1, 1).getId();
	}
	
	
	@Test
	public void populateSuccess(){
		AssignLiteralCell service = new AssignLiteralCell(U_USERNAME, CS_ID, CELL_ID0, LIT0);
		service.dispatch();
		
		assertEquals("Owner is correct", 
				U_USERNAME, getSpreadSheet(CS_NAME).getCreator().getUserName());
		assertEquals("Cell ID is correct",
				CELL_ID0, CS_SHEET.getCell(CELL_ID0).getId());
		assertEquals("Value is correct", 
				VAL0, CS_SHEET.getCell(CELL_ID0).getContent().getValue());
	}
	
	
	@Test(expected = NotFoundException.class)
	public void cellDoesntExist(){
		String literal_str = "0";
		String bad_cell_id = "123";
		
		AssignLiteralCell service = new AssignLiteralCell (U_USERNAME, CS_ID, bad_cell_id, literal_str);
		service.dispatch();
	}
	
	
	@Test(expected = NotFoundException.class)
	public void cellOutOfBonds(){
		int lines = 100;
		String literal_str = "0";
		String bad_cell_id = lines + ";1";
		
		assertTrue(CS_SHEET.getLines() < lines);
		AssignLiteralCell service = new AssignLiteralCell (U_USERNAME, CS_ID, bad_cell_id, literal_str);
		service.dispatch();
	}
	
	
	@Test(expected = NotFoundException.class)
	public void docDoesntExist(){
		int bad_doc_id = -123;
		
		AssignLiteralCell service = new AssignLiteralCell(U_USERNAME, bad_doc_id, CELL_ID0, LIT0);
		service.dispatch();
	}
	
	
	//FIXME: do we even have methods to protect cells?
	@Test(expected = PermissionException.class)
	public void cellIsProtected(){
		Cell cell = CS_SHEET.getCell(CELL_ID0);
		assertNotNull(cell);
		
		//FIXME: this isn't a proper way to change protection
		cell.setProtect(true);
		
		AssignLiteralCell service = new AssignLiteralCell(U_USERNAME, CS_ID, CELL_ID0, LIT0);
		try{
			service.dispatch();
		}
		catch(PermissionException Permexcept){
			cell.setProtect(false);
			throw Permexcept;
		}
	}
	
	
	@Test(expected = PermissionException.class)
	public void noWriteAccess(){
		User data_spy = createUser("NSA", "password", "National Security Agency");
		addUserToSession(U_USERNAME);
		
		AssignLiteralCell service = new AssignLiteralCell(data_spy.getUserName(), CS_ID, CELL_ID0, LIT0);
		service.dispatch();
		
	}
}
