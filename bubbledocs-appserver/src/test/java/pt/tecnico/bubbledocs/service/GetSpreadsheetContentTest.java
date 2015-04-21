package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;

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
	
	
	Literal L1 = new Literal(10);
	Reference R1;
	
	String [][] RESULT;
	
	@Override
	public void populate4Test(){
		createUser(U_UNAME, U_MAIL, U_PASS, U_NAME);
		U_TOKEN = addUserToSession(U_UNAME);
		USER = getUserFromUsername(U_UNAME);
		
		CreateSpreadSheet service = new CreateSpreadSheet(U_TOKEN, CS_NAME, CS_LINES, CS_COLUMNS);
		service.execute();
		CS = service.getResult();
		CS_ID = CS.getId();
		
		AssignLiteralCell alc = new AssignLiteralCell(U_TOKEN, CS_ID, "1;1", L1.toString());
		alc.execute();
		
		R1 = new Reference(CS.getCell("1;1"));
		AssignReferenceCell arc = new AssignReferenceCell(U_TOKEN, CS_ID, "2;2", R1.toString());
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
		assertEquals(RESULT.length, CS_COLUMNS);
		assertEquals(RESULT[0].length, CS_LINES);
	}
	
	
	@Test (expected=NotFoundException.class)
	public void badID(){
		String badID = "Ooops, this isnt a valid spreadsheet id.";
		
		GetSpreadsheetContentService gscs = new GetSpreadsheetContentService(
				U_TOKEN, badID);
		gscs.execute();
	}
	
	
	public void noID(){
		String nullID = null;
		
		GetSpreadsheetContentService gscs = new GetSpreadsheetContentService(
				U_TOKEN, nullID);
		gscs.execute();
	}
}
