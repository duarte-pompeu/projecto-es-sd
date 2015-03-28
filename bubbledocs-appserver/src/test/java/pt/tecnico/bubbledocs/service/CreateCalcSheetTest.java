package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import pt.tecnico.bubbledocs.dml.CalcSheet;
import pt.tecnico.bubbledocs.dml.User;

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
		
		assertEquals("User isnt in session",
				getUserFromSession(U_TOKEN).getUserName(),U_USERNAME);
		assertNotNull("calcsheet wasnt created", calc);
		assertEquals("Invalid owner",
				U_USERNAME, calc.getCreator().getUserName());
		assertEquals("Invalid calcsheet name",
				CS_NAME, calc.getName());
		assertEquals("Invalid calcsheet rows",
				CS_ROWS, (int) calc.getColumns());
		assertEquals("Invalid calcsheet lines",
				CS_LINES, (int) calc.getLines());
	}
	
	
	
}
