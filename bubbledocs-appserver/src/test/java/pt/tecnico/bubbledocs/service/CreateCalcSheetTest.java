package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.exceptions.InvalidValueException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;

public class CreateCalcSheetTest extends  BubbleDocsServiceTest {

	private final String U_USERNAME = "jubileu";
	private final String U_PASS = "password";
	private final String U_NAME = "Jubileu Mandafacas";
	private final String U_MAIL = "jubi_m@nomail.com";
	private String U_TOKEN;
	private final String CS_NAME = "Cabulas ES";
	private final int CS_COLUMNS = 10;
	private final int CS_LINES = 11;
	
	@Override
	public void populate4Test(){
		createUser(U_USERNAME, U_MAIL, U_PASS, U_NAME);
		U_TOKEN = addUserToSession(U_USERNAME);
		CreateSpreadSheet service = new CreateSpreadSheet(U_TOKEN, CS_NAME, CS_LINES, CS_COLUMNS);
		service.execute();
	}
	
	@Test
	public void populateSuccess(){
		CalcSheet calc = getSpreadSheet(CS_NAME);
		
		assertEquals("User isnt in session",
				getUserFromSession(U_TOKEN).getUserName(),U_USERNAME);
		assertNotNull("calcsheet wasnt created", calc);
		assertEquals("Invalid owner",
				U_USERNAME, calc.getCreator().getUserName());
		assertEquals("Invalid calcsheet name",
				CS_NAME, calc.getName());
		assertEquals("Invalid calcsheet rows",
				CS_COLUMNS, (int) calc.getColumns());
		assertEquals("Invalid calcsheet lines",
				CS_LINES, (int) calc.getLines());
	}
	
	
	@Test
	public void assertAllCells(){
		CalcSheet calc = getSpreadSheet(CS_NAME);
		
		for(int c = 1; c <= CS_COLUMNS; c++){
			for(int l = 1; l <= CS_LINES; l++){
				Cell cell = calc.getCell(l,c);
				assertEquals(new Integer(c), new Integer(cell.getColumn()));
				assertEquals(new Integer(l), new Integer(cell.getLine()));
			}
		}
	}
	
	
	@Test(expected = UserNotInSessionException.class)
	public void noLogin(){
		String bad_session_token = "Bad session token";
		CreateSpreadSheet service = new CreateSpreadSheet(bad_session_token, CS_NAME, CS_COLUMNS, CS_LINES);
		service.execute();
	}
	
	@Test(expected = NullPointerException.class)
	public void nullName(){
		String bad_name = null;
		CreateSpreadSheet service = new CreateSpreadSheet(U_TOKEN, bad_name, CS_COLUMNS, CS_LINES);
		service.execute();
	}
	
	@Test(expected = InvalidValueException.class)
	public void wrongRowValueFrontier(){
		int bad_value = 0;
		CreateSpreadSheet service = new CreateSpreadSheet(U_TOKEN, CS_NAME, bad_value, CS_LINES);
		service.execute();
	}
	
	@Test(expected = InvalidValueException.class)
	public void wrongRowValue(){
		int bad_value = -1;
		CreateSpreadSheet service = new CreateSpreadSheet(U_TOKEN, CS_NAME, bad_value, CS_LINES);
		service.execute();
	}
	
	@Test(expected = InvalidValueException.class)
	public void wrongColumnValueFrontier(){
		int bad_value = 0;
		CreateSpreadSheet service = new CreateSpreadSheet(U_TOKEN, CS_NAME, CS_COLUMNS, bad_value);
		service.execute();
	}
	
	@Test(expected = InvalidValueException.class)
	public void wrongColumnValue(){
		int bad_value = -1;
		CreateSpreadSheet service = new CreateSpreadSheet(U_TOKEN, CS_NAME, CS_COLUMNS, bad_value);
		service.execute();
	}
	
}
