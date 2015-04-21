package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.InvalidFormatException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;

public class GetSpreadsheetContentTest extends BubbleDocsServiceTest {
	private static final String U_UNAME = "pompas";
	private static final String U_MAIL = "pompas@mail";
	private static final String U_NAME = "Duarte";
	private static final String U_PASS = "password";
	private static User USER;
	private String U_TOKEN;
	
	private static CalcSheet CS;
	private final String CS_NAME = "Cabulas ES";
	private static int CS_ID;
	private final int CS_COLUMNS = 10;
	private final int CS_LINES = 11;
	
	
	private static final String L1 = "5";
	private Reference R1;
	
	private String [][] RESULT;
	
	@Override
	public void populate4Test(){
		createUser(U_UNAME, U_MAIL, U_PASS, U_NAME);
		U_TOKEN = addUserToSession(U_UNAME);
		USER = getUserFromUsername(U_UNAME);
		
		CreateSpreadSheet service = new CreateSpreadSheet(U_TOKEN, CS_NAME, CS_LINES, CS_COLUMNS);
		service.execute();
		CS = service.getResult();
		CS_ID = CS.getId();
		
		AssignLiteralCell alc = new AssignLiteralCell(U_TOKEN, CS_ID, CS.getCell(1, 1).getId(), L1);
		alc.execute();
		
		R1 = new Reference(CS.getCell(1,1));
		AssignReferenceCell arc = new AssignReferenceCell(U_TOKEN, CS_ID, CS.getCell(2, 2).getId(), "1;1");
		arc.dispatch();
		
		GetSpreadsheetContentService gscs = new GetSpreadsheetContentService(
				U_TOKEN, Integer.toString(CS_ID));
		gscs.execute();
		RESULT = gscs.getResult();
	}
	
	
	@Test
	public void populateSuccess(){
		assertEquals(L1.toString(), RESULT[0][0]);
		assertEquals(R1.toString(), RESULT[1][1]);
		assertEquals(RESULT[0].length, CS_COLUMNS);
		assertEquals(RESULT.length, CS_LINES);
	}
	
	
	@Test (expected=NotFoundException.class)
	public void badID(){
		String badID = "987";
		
		GetSpreadsheetContentService gscs = new GetSpreadsheetContentService(
				U_TOKEN, badID);
		gscs.execute();
	}
	
	
	@Test (expected=InvalidFormatException.class)
	public void noID(){
		String nullID = null;
		
		GetSpreadsheetContentService gscs = new GetSpreadsheetContentService(
				U_TOKEN, nullID);
		gscs.execute();
	}
	
	
	@Test (expected=PermissionException.class)
	public void badAccess(){
		createUser("temp", "temp@temporary.temp", "temppass", "TempName");
		String tmpToken = addUserToSession("temp");
		
		GetSpreadsheetContentService gscs = new GetSpreadsheetContentService(
				tmpToken, Integer.toString(CS_ID));
		gscs.execute();
	}
}
