package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.tecnico.bubbledocs.dml.CalcSheet;
import pt.tecnico.bubbledocs.dml.Cell;
import pt.tecnico.bubbledocs.dml.User;
import pt.tecnico.bubbledocs.exceptions.InvalidFormatException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;

public class CreateCalcSheetTest extends  BubbleDocsServiceTest {

	private final String U_USERNAME = "jubileu";
	private final String U_PASS = "password";
	private final String U_NAME = "Jubileu Mandafacas";
	private String U_TOKEN;
	private User USER;
	private final String CS_NAME = "Cabulas ES";
	private final int CS_ROWS = 10;
	private final int CS_LINES = 10;
	
	@Override
	public void populate4Test(){
		USER = createUser(U_USERNAME, U_PASS, U_NAME);
		U_TOKEN = addUserToSession(U_USERNAME);
		
	}
	
	@Test
	public void populateSuccess(){
		CreateSpreadSheet service = new CreateSpreadSheet(U_TOKEN, CS_NAME, CS_ROWS, CS_LINES);
		service.dispatch();
		CalcSheet calc = getSpreadSheet(CS_NAME);
		
		assertEquals("User is in session",getUserFromSession(U_TOKEN).getUserName(),U_USERNAME);
		
	}
	
	
	
}
