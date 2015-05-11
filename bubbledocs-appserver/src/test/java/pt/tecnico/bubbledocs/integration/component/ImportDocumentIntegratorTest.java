package pt.tecnico.bubbledocs.integration.component;

import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.service.BubbleDocsServiceTest;
import pt.tecnico.bubbledocs.service.ExportDocument;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.junit.Test;

import pt.tecnico.bubbledocs.domain.Add;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.CalcSheetExporter;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Content;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.LiteralArgument;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.ReferenceArgument;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.CannotLoadDocumentException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.integration.ImportDocumentIntegrator;


public class ImportDocumentIntegratorTest extends BubbleDocsServiceTest {
	
	private final String U_USERNAME = "jubileu";
	private final String U_USERNAME2 = "olivar";
	private final String U_PASS = "password";
	private final String U_NAME = "Jubileu Mandafacas";
	private String U_TOKEN;
	private String no_permission_token;
	private User USER;
	private CalcSheet CS_EMPTY;
	private CalcSheet CS_ONE;
	private CalcSheet CS_MULTIPLE;
	private int CS_ID;
	private int CS_ID2;
	private int CS_ID3;
	private String CS_NAME = "cs";
	private String CS_NAME2 = "name2";
	private String CS_NAME3 = "name3";
	private final int CS_ROWS = 3;
	private final int CS_LINES = 3;
	
	private String CELL_ID0;
	private String CELL_ID1;
	private String CELL_ID2;
	private String CELL_ID3;
	private String U_EMAIL="email@email.com";
	private String U_EMAIL2="email2@email.com";
	
	@Mocked
	StoreRemoteServices remote;
	
	@Override
	public void populate4Test(){
		USER = createUser(U_USERNAME, U_PASS, U_NAME, U_EMAIL);
		U_TOKEN = addUserToSession(U_USERNAME);
	
		createUser(U_USERNAME2, U_PASS, U_NAME, U_EMAIL2);
		no_permission_token = addUserToSession(U_USERNAME2);
		
		
		//Empty spreadsheet
		CS_EMPTY = createSpreadSheet(USER, CS_NAME, CS_ROWS, CS_LINES);
		CS_ID = CS_EMPTY.getId();
		
		//Spreadsheet with one cell
		CS_ONE = createSpreadSheet(USER, CS_NAME2, CS_ROWS, CS_LINES);
		CS_ID2 = CS_ONE.getId();
		CELL_ID0 = this.getSpreadSheet(CS_NAME2).getCell(1, 1).getId();
		this.getSpreadSheet(CS_NAME2).getCell(CELL_ID0).setContent(new Literal(7));
		
		//Spreadsheet with multiple cells
		CS_MULTIPLE = createSpreadSheet(USER, CS_NAME3, CS_ROWS, CS_LINES);
		CS_ID3 = CS_MULTIPLE.getId();
		CELL_ID1 = this.getSpreadSheet(CS_NAME3).getCell(1, 1).getId();
		CELL_ID2 = this.getSpreadSheet(CS_NAME3).getCell(1, 2).getId();
		CELL_ID3 = this.getSpreadSheet(CS_NAME3).getCell(2, 1).getId();
		
		Cell c1 = this.getSpreadSheet(CS_NAME3).getCell(CELL_ID1);
		c1.setContent(new Literal(7));
		
		this.getSpreadSheet(CS_NAME3).getCell(CELL_ID2)
			.setContent(new Reference(c1));
		
		this.getSpreadSheet(CS_NAME3).getCell(CELL_ID3)
			.setContent(new Add(new ReferenceArgument(c1), new LiteralArgument(5)));
	
	}
	
	@Test
	public void emptyCalcSheet() throws JDOMException, IOException{
		byte[] exported = new CalcSheetExporter().exportToXmlData(CS_EMPTY);
		
		new Expectations() {{
			remote.loadDocument(U_USERNAME, CS_NAME);
			result = exported; times =1;
		}};		
		
		ImportDocumentIntegrator service = new ImportDocumentIntegrator(U_TOKEN, CS_ID);
		service.execute();
		
		assertNotEquals("old id and new id are the same", service.getOldDocId(), service.getNewDocId());
		
		CalcSheet oldSheet = CS_EMPTY;
		
		//O novo CalcSheet tem de existir.
		CalcSheet newSheet = BubbleDocs.getInstance().getCalcSheetById(service.getNewDocId());
		
		assertEquals("creators are different", oldSheet.getCreator().getUserName(), newSheet.getCreator().getUserName());
		assertEquals("names are different", oldSheet.getName(), newSheet.getName());
		assertEquals("dates are different", oldSheet.getDate(), newSheet.getDate());
		assertEquals("lines are different", oldSheet.getLines(), newSheet.getLines());
		assertEquals("columns are different", oldSheet.getColumns(), newSheet.getLines());
		
		for (Cell oldCell : oldSheet.getCellSet()) {
			Cell newCell = newSheet.getCell(oldCell.getId());
			assertNull(oldCell.getContent());
			assertNull(newCell.getContent());
		}
	}
	

	
	@Test
	public void calcSheetWithOneCell() throws JDOMException, IOException{
		byte[] exported = new CalcSheetExporter().exportToXmlData(CS_ONE);
		
		new Expectations() {{
			remote.loadDocument(U_USERNAME, CS_NAME2);
			result = exported; times =1;
		}};	
		
		ImportDocumentIntegrator service = new ImportDocumentIntegrator(U_TOKEN, CS_ID2);
		service.execute();

		assertNotEquals("old id and new id are the same", service.getOldDocId(), service.getNewDocId());
		
		CalcSheet oldSheet = CS_ONE;
		
		//O novo CalcSheet tem de existir.
		CalcSheet newSheet = BubbleDocs.getInstance().getCalcSheetById(service.getNewDocId());
		
		assertEquals("creators are different", oldSheet.getCreator().getUserName(), newSheet.getCreator().getUserName());
		assertEquals("names are different", oldSheet.getName(), newSheet.getName());
		assertEquals("dates are different", oldSheet.getDate(), newSheet.getDate());
		assertEquals("lines are different", oldSheet.getLines(), newSheet.getLines());
		assertEquals("columns are different", oldSheet.getColumns(), newSheet.getLines());
		
		Content newContent = newSheet.getCell(CELL_ID0).getContent();
		assertTrue("it's not a Literal", newContent instanceof Literal);
		assertEquals("value is not a literal 7", 7, newContent.getValue());
	}
	
	@Test
	public void calcSheetWithMultipleCells() throws JDOMException, IOException{
		byte[] exported = new CalcSheetExporter().exportToXmlData(CS_MULTIPLE);
		
		new Expectations() {{
			remote.loadDocument(U_USERNAME, CS_NAME3);
			result = exported; times =1;
		}};			
		
		ImportDocumentIntegrator service = new ImportDocumentIntegrator(U_TOKEN, CS_ID3);
		service.execute();

		assertNotEquals("old id and new id are the same", service.getOldDocId(), service.getNewDocId());
		
		CalcSheet oldSheet = CS_MULTIPLE;
		
		//O novo CalcSheet tem de existir.
		CalcSheet newSheet = BubbleDocs.getInstance().getCalcSheetById(service.getNewDocId());
		
		assertEquals("creators are different", oldSheet.getCreator().getUserName(), newSheet.getCreator().getUserName());
		assertEquals("names are different", oldSheet.getName(), newSheet.getName());
		assertEquals("dates are different", oldSheet.getDate(), newSheet.getDate());
		assertEquals("lines are different", oldSheet.getLines(), newSheet.getLines());
		assertEquals("columns are different", oldSheet.getColumns(), newSheet.getLines());
		
		Content newContent1 = newSheet.getCell(CELL_ID1).getContent();
		assertTrue("it's not a Literal", newContent1 instanceof Literal);
		assertEquals("value is not a literal 7", 7, newContent1.getValue());
		
		Content newContent2 = newSheet.getCell(CELL_ID2).getContent();
		assertTrue("it's not a reference", newContent2 instanceof Reference);
		assertEquals("the reference has the wrong value", 7, newContent2.getValue());
		
		Content newContent3 = newSheet.getCell(CELL_ID3).getContent();
		assertTrue("it's not and add function", newContent3 instanceof Add);
		assertEquals("add has the wrong value", 12, newContent3.getValue());
	}
	


	
	//Testing the case of trying to import an existing spread sheet and SD-STORE being unavailable
	@Test(expected = UnavailableServiceException.class)
	public void storeServiceUnavailable(){
		new Expectations() {{
			remote.loadDocument(U_USERNAME, CS_NAME);
			result = new RemoteInvocationException();
		}};
		ImportDocumentIntegrator service = new ImportDocumentIntegrator(U_TOKEN, CS_ID);
		service.execute();
	}
	
	//Testing the case of trying to import an existing spread sheet and SD-STORE being unable to load it
	@Test(expected = CannotLoadDocumentException.class)
	public void storeServiceCantLoad(){
		new Expectations() {{
			remote.loadDocument(U_USERNAME, CS_NAME);
			result = new CannotLoadDocumentException(); 
		}};
		ImportDocumentIntegrator service = new ImportDocumentIntegrator(U_TOKEN, CS_ID);
		service.execute();
	}
		

	//Testing the case of trying to import an existing spread sheet with a user without permission (not the owner)
	@Test(expected = PermissionException.class)
	public void noPermission(){
		new Expectations() {{
			remote.loadDocument(anyString, anyString);
			times = 0;
		}};
		
		ImportDocumentIntegrator service = new ImportDocumentIntegrator(no_permission_token, CS_ID);
		service.execute();
	}

}



