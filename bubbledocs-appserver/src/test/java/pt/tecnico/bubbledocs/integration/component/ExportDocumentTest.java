package pt.tecnico.bubbledocs.integration.component;

import pt.tecnico.bubbledocs.service.BubbleDocsServiceTest;
import pt.tecnico.bubbledocs.service.remote.*;
import mockit.*;
import org.junit.Test;
import pt.tecnico.bubbledocs.domain.Add;
import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.CalcSheetExporter;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.LiteralArgument;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.ReferenceArgument;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.CannotStoreDocumentException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.integration.ExportDocumentIntegrator;

public class ExportDocumentTest extends BubbleDocsServiceTest {
	
	private final String U_USERNAME = "jubileu";
	private final String U_USERNAME2 = "olivar";
	private final String U_PASS = "password";
	private final String U_NAME = "Jubileu Mandafacas";
	private String U_TOKEN;
	private String no_permission_token;
	private User USER;
	private CalcSheet CS_EMPTY;
	private int CS_ID;
	private final String CS_NAME = "cs";
	private final int CS_ROWS = 3;
	private final int CS_LINES = 3;
	
	private String CELL_ID0;
	private String CELL_ID1;
	private String CELL_ID2;
	private String U_EMAIL="email@email.com";
	private String U_EMAIL2="email2@email.com";
	private byte[] EXPORTED;

	@Mocked
	StoreRemoteServices remote;
		
	@Override
	public void populate4Test(){
		USER = createUser(U_USERNAME, U_PASS, U_NAME, U_EMAIL);
		U_TOKEN = addUserToSession(U_USERNAME);
	
		createUser(U_USERNAME2, U_PASS, U_NAME, U_EMAIL2);
		no_permission_token = addUserToSession(U_USERNAME2);
		
		CS_EMPTY = createSpreadSheet(USER, CS_NAME, CS_ROWS, CS_LINES);
		CS_ID = CS_EMPTY.getId();
		CELL_ID0 = this.getSpreadSheet(CS_NAME).getCell(1, 1).getId();
		CELL_ID1 = this.getSpreadSheet(CS_NAME).getCell(1, 2).getId();
		CELL_ID2 = this.getSpreadSheet(CS_NAME).getCell(2, 1).getId();
		EXPORTED = new CalcSheetExporter().exportToXmlData(CS_EMPTY);
	}
	
	
	@Test
	public void emptyCalcSheet() throws Exception {		
		
		
		new Expectations() {{
			remote.storeDocument(U_USERNAME, CS_NAME, EXPORTED); times = 1;
		}};
		
		ExportDocumentIntegrator service = new ExportDocumentIntegrator(U_TOKEN, CS_ID);
		service.execute();
	}
	
	
	@Test
	public void calcSheetWithOneCell() throws Exception {
		Cell c=this.getSpreadSheet(CS_NAME).getCell(CELL_ID0);
		c.setContent(new Literal(7));
		byte[] exported = new CalcSheetExporter().exportToXmlData(CS_EMPTY);
		
		new Expectations() {{
			remote.storeDocument(U_USERNAME, CS_NAME, exported); times = 1;
		}};
		
		ExportDocumentIntegrator service = new ExportDocumentIntegrator(U_TOKEN, CS_ID);
		service.execute();
	}
	
	@Test
	public void calcSheetWithMultipleCells() throws Exception {
		Cell c1=this.getSpreadSheet(CS_NAME).getCell(CELL_ID0);
		c1.setContent(new Literal(7));
		Cell c2=this.getSpreadSheet(CS_NAME).getCell(CELL_ID1);
		c2.setContent(new Reference(c1));
		Cell c3=this.getSpreadSheet(CS_NAME).getCell(CELL_ID2);
		c3.setContent(new Add(new ReferenceArgument(c1), new LiteralArgument(5)));
		byte[] exported = new CalcSheetExporter().exportToXmlData(CS_EMPTY);
		
		new Expectations() {{
			remote.storeDocument(U_USERNAME, CS_NAME, exported); times = 1;
		}};
		
		ExportDocumentIntegrator service = new ExportDocumentIntegrator(U_TOKEN, CS_ID);
		service.execute();
	}
	
	//Testing the case of trying to export a non existing spread sheet
	@Test(expected= NotFoundException.class)
	public void nonExistingCalcSheet() throws Exception {
		new Expectations() {{
			remote.storeDocument(anyString, anyString, (byte[]) any); times = 0;
		}};
		
		ExportDocumentIntegrator service = new ExportDocumentIntegrator(U_TOKEN, -1);
		service.execute();
	}
		
	//Testing the case of trying to export an existing spread sheet with an invalid login token
	@Test(expected = UserNotInSessionException.class)
	public void noLogin(){
		new Expectations() {{
			remote.storeDocument(anyString, anyString, (byte[]) any); times = 0;
		}};
		
		String bad_session_token = "Bad session token";
		ExportDocumentIntegrator service = new ExportDocumentIntegrator(bad_session_token, CS_ID);
		service.execute();
	}
	
	

	
	//Testing the case of trying to export an existing spread sheet and SD-STORE being unavailable
	@Test(expected = UnavailableServiceException.class)
	public void storeServiceUnavailable(){
		new Expectations() {{
			remote.storeDocument(U_USERNAME, CS_NAME, EXPORTED); times = 1;
			result = new RemoteInvocationException();
		}};
		
		ExportDocumentIntegrator service = new ExportDocumentIntegrator(U_TOKEN, CS_ID);
		service.execute();
	}
	
	//Testing the case of trying to export an existing spread sheet and SD-STORE being unable to store it
	@Test(expected = CannotStoreDocumentException.class)
	public void storeServiceCantStore(){
		new Expectations() {{
			remote.storeDocument(U_USERNAME, CS_NAME, EXPORTED); times = 1;
			result = new CannotStoreDocumentException();
		}};
		
		ExportDocumentIntegrator service = new ExportDocumentIntegrator(U_TOKEN, CS_ID);
		service.execute();
	}
		

	//Testing the case of trying to export an existing spread sheet with a user without permission
	@Test(expected = PermissionException.class)
	public void noPermission(){
		new Expectations() {{
			remote.storeDocument(anyString, anyString, (byte[]) any); times = 0;
		}};
		
		ExportDocumentIntegrator service = new ExportDocumentIntegrator(no_permission_token, CS_ID);
		service.execute();
	}
	
	
	
	}



