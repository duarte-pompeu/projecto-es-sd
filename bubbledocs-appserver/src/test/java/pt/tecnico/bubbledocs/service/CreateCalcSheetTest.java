package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import pt.tecnico.bubbledocs.dml.CalcSheet;
import pt.tecnico.bubbledocs.dml.User;
import pt.tecnico.bubbledocs.exceptions.InvalidFormatException;
import pt.tecnico.bubbledocs.exceptions.InvalidValueException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;

public class CreateCalcSheetTest extends  BubbleDocsServiceTest {

	private final String U_USERNAME = "jubileu";
	private final String U_PASS = "password";
	private final String U_NAME = "Jubileu Mandafacas";
	private String U_TOKEN;
	private final String CS_NAME = "Cabulas ES";
	private final int CS_ROWS = 10;
	private final int CS_LINES = 10;
	
	@Override
	public void populate4Test(){
		createUser(U_USERNAME, U_PASS, U_NAME);
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
	
	@Test(expected = UserNotInSessionException.class)
	public void noLogin(){
		String bad_session_token = "Bad session token";
		CreateSpreadSheet service = new CreateSpreadSheet(bad_session_token, CS_NAME, CS_ROWS, CS_LINES);
		service.dispatch();
	}
	
	@Test(expected = InvalidFormatException.class)
	public void nullName(){
		String bad_name = null;
		CreateSpreadSheet service = new CreateSpreadSheet(U_TOKEN, bad_name, CS_ROWS, CS_LINES);
		service.dispatch();
	}
	
	@Test(expected = InvalidValueException.class)
	public void wrongRowValueFrontier(){
		int bad_value = 0;
		CreateSpreadSheet service = new CreateSpreadSheet(U_TOKEN, CS_NAME, bad_value, CS_LINES);
		service.dispatch();
	}
	
	@Test(expected = InvalidValueException.class)
	public void wrongRowValue(){
		int bad_value = -1;
		CreateSpreadSheet service = new CreateSpreadSheet(U_TOKEN, CS_NAME, bad_value, CS_LINES);
		service.dispatch();
	}
	
	@Test(expected = InvalidValueException.class)
	public void wrongColumnValueFrontier(){
		int bad_value = 0;
		CreateSpreadSheet service = new CreateSpreadSheet(U_TOKEN, CS_NAME, CS_ROWS, bad_value);
		service.dispatch();
	}
	
	@Test(expected = InvalidValueException.class)
	public void wrongColumnValue(){
		int bad_value = -1;
		CreateSpreadSheet service = new CreateSpreadSheet(U_TOKEN, CS_NAME, CS_ROWS, bad_value);
		service.dispatch();
	}
	
}
