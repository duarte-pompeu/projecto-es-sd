package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.*;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.BinaryFunction;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.exceptions.InvalidFormatException;
import pt.tecnico.bubbledocs.exceptions.InvalidValueException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;

public class AssignBinaryToCellTest extends BubbleDocsServiceTest {


	@Mocked
	IDRemoteServices remote;

	private final String U_USERNAME = "Maicou";
	private final String U_PASS = "password";
	private final String U_NAME = "Maicou Feupes";
	private final String U_MAIL = "maicou_feupes@suimepule.com";
	private String u_token;

	private User USER;

	private String Function_Expression =  "=ADD(2,3;1)";
	private CalcSheet CS_SHEET;
	private int CS_ID;
	private final String CS_NAME = "Teste Referencia";
	private final int CS_ROWS = 10;
	private final int CS_LINES = 11;

	private String CELL_ID0;


	@Override
	public void populate4Test() {
		USER = createUser(U_USERNAME, U_MAIL, U_PASS, U_NAME);
		u_token = addUserToSession(U_USERNAME);

		CS_SHEET = createSpreadSheet(USER, CS_NAME, CS_ROWS, CS_LINES);
		CS_ID = CS_SHEET.getId();
		CELL_ID0 = CS_SHEET.getCell(1, 1).getId();
		CS_SHEET.getCell(3,1).setContent(new Literal("3"));
	}

	@Test
	public void Success() {
		AssignBinaryToCell service = new AssignBinaryToCell( CELL_ID0, Function_Expression ,CS_ID, u_token);
		service.execute();

		assertEquals("Content value is incorrect", service.getResult(), "5");
	}

	@Test(expected = UserNotInSessionException.class)
	public void NoSession(){
		String bad_token = "RandomToken";
		AssignBinaryToCell service = new AssignBinaryToCell(CELL_ID0, Function_Expression, CS_ID, bad_token);
		service.execute();
	}

	@Test(expected = NotFoundException.class)
	public void DocDoesntExist(){
		int bad_cs_id = -9000; 
		AssignBinaryToCell service = new AssignBinaryToCell( CELL_ID0, Function_Expression,bad_cs_id, u_token);
		service.execute();
	}

	@Test(expected = NotFoundException.class)
	public void CellDoesntExist(){
		String bad_cell_id = "666";
		AssignBinaryToCell service = new AssignBinaryToCell( bad_cell_id, Function_Expression, CS_ID, u_token);
		service.execute();
	}

	@Test (expected = NotFoundException.class)
	public void outOfMinBounds(){
		String cellID = "0;0";
		AssignBinaryToCell service = new AssignBinaryToCell( cellID, Function_Expression, CS_ID, u_token);
		service.execute();
	}

	@Test (expected = NotFoundException.class)
	public void outOfMaxBounds(){
		String cellID = Integer.toString(CS_LINES) + ";" + Integer.toString(CS_ROWS);
		AssignBinaryToCell service = new AssignBinaryToCell( cellID, Function_Expression, CS_ID, u_token);
		service.execute();
	}



	@Test(expected = PermissionException.class)
	public void NoPermissions(){
		User imqt = createUser("imqtpie", "qtpie@example.com", "easypass", "Cutie Pie");
		String new_token = addUserToSession(imqt.getUserName());
		AssignBinaryToCell service = new AssignBinaryToCell( CELL_ID0, Function_Expression, CS_ID, new_token);
		service.execute();
	}


	@Test(expected = InvalidFormatException.class)
	public void InvalidFunction(){
		String bad_string = "this is not a function";
		AssignBinaryToCell service = new AssignBinaryToCell( CELL_ID0, bad_string, CS_ID, u_token);
		service.execute();
	}
	
	@Test
	public void invalidReference() {
		String invalidReference = "=ADD(2,1;3)";
		AssignBinaryToCell service = new AssignBinaryToCell(CELL_ID0, invalidReference, CS_ID, u_token);
		service.execute();
		
		assertEquals("This didn't return #VALUE", service.getResult(), "#VALUE");
	}

}
