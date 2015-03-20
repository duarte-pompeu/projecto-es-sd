package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.dml.CalcSheet;
import pt.tecnico.bubbledocs.dml.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;

public class AssignLiteralCellTest extends BubbleDocsServiceTest {
	String userName;
	int docId;
	
	private final String U_NAME = "jubileu";
	private final String U_PASS = "password";
	
	private User USER;
	
	private CalcSheet SH_SHEET;
	private int SH_ID;
	private final String SH_NAME = "Cabulas ES";
	private final int SH_ROWS = 10;
	private final int SH_LINES = 10;
	
	private String CELL_ID0;
	private final int VAL0 = 0;
	private final String LIT0 = "0";

	
	@Override
	// TODO: login user before inserting
	// TODO: create a calcsheet
	public void populate4Test(){
		USER = createUser(U_NAME, U_PASS, "Jubileu Mandafacas");
		addUserToSession(U_NAME);
		
		SH_SHEET = createSpreadSheet(USER, SH_NAME, SH_ROWS, SH_LINES);
		SH_ID = SH_SHEET.getId();
		CELL_ID0 = SH_SHEET.getCell(1, 1).getId();
	}
	
	
	@Test
	public void simpleSuccess(){
		AssignLiteralCell service = new AssignLiteralCell(U_NAME, SH_ID, CELL_ID0, LIT0);
		service.dispatch();
		
		assertEquals("Owner is correct", U_NAME, getSpreadSheet(SH_NAME).getCreator().getUserName());
		assertEquals(VAL0, SH_SHEET.getCell(CELL_ID0).getContent().getValue());
	}
	
	
	@Test(expected = BubbleDocsException.class)
	public void cellDoesntExist(){
		String int_as_string = "0";
		
		// if you change this value, make sure it remains a BAD cell id
		String bad_cell_id = "123";
		
		AssignLiteralCell service = new AssignLiteralCell (userName, docId, bad_cell_id, int_as_string);
		service.dispatch();
	}
	
	
	@Test(expected = BubbleDocsException.class)
	public void docDoesntExist(){
		// make sure this doesnt exist
		int bad_doc = -123;
		
		AssignLiteralCell service = new AssignLiteralCell(U_NAME, bad_doc, CELL_ID0, LIT0);
		service.dispatch();
	}
	
	
	@Test(expected = BubbleDocsException.class)
	//TODO: ALCTest complete stub method
	public void cellIsProtected(){
		
	}
	
	
	@Test(expected = BubbleDocsException.class)
	//TODO: ALCTest complete stub method
	public void noWriteAccess(){
		
	}
}
