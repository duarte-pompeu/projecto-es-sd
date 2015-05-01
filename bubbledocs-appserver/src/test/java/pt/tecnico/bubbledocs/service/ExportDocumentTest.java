package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import pt.tecnico.bubbledocs.service.remote.*;
import mockit.*;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
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
	public void emptyCalcSheet() throws JDOMException, IOException{		
		
		
		new Expectations() {{
			remote.storeDocument(U_USERNAME, CS_NAME, EXPORTED); times = 1;
		}};
		
		ExportDocument service = new ExportDocument(U_TOKEN, CS_ID);
		service.execute();
		
		//a local setup
		SAXBuilder b=new SAXBuilder();
		Document xmlDoc=b.build(new ByteArrayInputStream(service.getDocXML()));
		
        XPathFactory xFactory = XPathFactory.instance();

        XPathExpression<Element> expr = xFactory.compile("/calcSheet", Filters.element());
        List<Element> links = expr.evaluate(xmlDoc);
        Element sheetElement=links.get(0);
      		
		String nameReadFromDocument= sheetElement.getAttribute("creator").getValue();
        int spreadSheetId= sheetElement.getAttribute("id").getIntValue();
		String spreadSheetDate= sheetElement.getAttribute("date").getValue();
        String spreadSheetName= sheetElement.getAttribute("name").getValue();
        int spreadSheetLines = sheetElement.getAttribute("lines").getIntValue();
        int spreadSheetColumns = sheetElement.getAttribute("columns").getIntValue();
		
		//asserting all the attribute values
		assertEquals("Owner is NOT correct", U_USERNAME, nameReadFromDocument);
		assertEquals("ID is NOT correct", this.getSpreadSheet(CS_NAME).getId().intValue(), spreadSheetId );
		assertEquals("Creation date is NOT correct", 
				this.getSpreadSheet(CS_NAME).getDate().toString(), spreadSheetDate );
		assertEquals("Name is NOT correct", this.getSpreadSheet(CS_NAME).getName(), spreadSheetName );
		assertEquals("The number of lines is NOT correct", 
				this.getSpreadSheet(CS_NAME).getLines().intValue(), spreadSheetLines );
		assertEquals("The number of columns is NOT correct", 
				this.getSpreadSheet(CS_NAME).getColumns().intValue(), spreadSheetColumns );
		
		//asserting the correct number of cells
		assertEquals("The number of cells is correct", 0, sheetElement.getChildren().size());
		
		
		//asserting that all the cells have null contents
		for(Element cellElement: sheetElement.getChildren()){
			//FIXME: we have 0 cells, so this loop doesn't execute
			assertEquals("empty cell", 0 ,cellElement.getContentSize());	
		}
		
	}
	
	
	@Test
	public void calcSheetWithOneCell() throws JDOMException, IOException{
		Cell c=this.getSpreadSheet(CS_NAME).getCell(CELL_ID0);
		c.setContent(new Literal(7));
		byte[] exported = new CalcSheetExporter().exportToXmlData(CS_EMPTY);
		
		new Expectations() {{
			remote.storeDocument(U_USERNAME, CS_NAME, exported); times = 1;
		}};
		
		ExportDocument service = new ExportDocument(U_TOKEN, CS_ID);
		service.execute();
		
		//a local setup
		SAXBuilder b=new SAXBuilder();
		Document xmlDoc=b.build(new ByteArrayInputStream(service.getDocXML()));
		
        XPathFactory xFactory = XPathFactory.instance();

        XPathExpression<Element> expr = xFactory.compile("/calcSheet", Filters.element());
        List<Element> links = expr.evaluate(xmlDoc);
        Element sheetElement=links.get(0);
        List<Element> cellsList=sheetElement.getChildren();
        Element literalElement=null;
        for(Element cellElement: cellsList){
			if(cellElement.getChild("literal")!=null){
				literalElement=cellElement.getChild("literal");
				break;
			}
		}
      
		
        //asserting the literal cell exists and has the correct value
		assertEquals("literal cell is wrong", 7 ,literalElement.getAttribute("val").getIntValue());
		
		int counter=0;
		//asserting that all the other cells have empty contents
		for(Element cellElement: sheetElement.getChildren()){
			if(cellElement.getContentSize()!=0)
				counter++;
		}
		assertEquals("non empty cells", counter, 1);
		
	}
	
	@Test
	public void calcSheetWithMultipleCells() throws JDOMException, IOException{
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
		
		ExportDocument service = new ExportDocument(U_TOKEN, CS_ID);
		service.execute();
		
		//a local setup
		SAXBuilder b=new SAXBuilder();
		Document xmlDoc=b.build(new ByteArrayInputStream(service.getDocXML()));
		
        XPathFactory xFactory = XPathFactory.instance();

        XPathExpression<Element> expr = xFactory.compile("/calcSheet", Filters.element());
        List<Element> links = expr.evaluate(xmlDoc);
        Element sheetElement=links.get(0);
        List<Element> cellsList=sheetElement.getChildren();
        Element literalElement=null;
        Element referenceElement=null;
        Element addElement=null;
        for(Element cellElement: cellsList){
        	if(cellElement.getChild("literal")!=null){
				literalElement=cellElement.getChild("literal");
				break;
			}
        }
        for(Element cellElement: cellsList){
        	if(cellElement.getChild("reference")!=null){
				referenceElement=cellElement.getChild("reference");
				break;
			}
        }
        for(Element cellElement: cellsList){
        	if(cellElement.getChild("add")!=null){
				addElement=cellElement.getChild("add");
				break;
			}
        }
        
        Element pointedLiteralElement=referenceElement.getChild("cell").getChild("literal");
        Element arg1Element=addElement.getChild("referenceArgument");
        Element pointedLiteralElement2=arg1Element.getChild("cell").getChild("literal");
        Element arg2Element=addElement.getChild("literalArgument");
		
        //asserting the literal cell exists and has the correct value
		assertEquals("literal cell is wrong", 7 ,literalElement.getAttribute("val").getIntValue());
		//asserting the reference is pointing to the literal
		assertEquals("reference DOES NOT point to the literal", 7, 
				pointedLiteralElement.getAttribute("val").getIntValue());
		assertEquals("add element DOES NOT HAVE the correct reference and literal", 12, 
				pointedLiteralElement2.getAttribute("val").getIntValue()+arg2Element.getAttribute("val").getIntValue());
		
		
		int counter=0;
		
		for(Element cellElement: sheetElement.getChildren()){
			if(cellElement.getContentSize()!=0)
				counter++;
		}
		//asserting that all the other cells have empty contents
		assertEquals("non empty cells", counter, 3);
		
	}
	
	//Testing the case of trying to export a non existing spread sheet
	@Test(expected= NotFoundException.class)
	public void nonExistingCalcSheet() throws JDOMException, IOException{
		new Expectations() {{
			remote.storeDocument(anyString, anyString, (byte[]) any); times = 0;
		}};
		
		ExportDocument service = new ExportDocument(U_TOKEN, -1);
		service.execute();
	}
		
	//Testing the case of trying to export an existing spread sheet with an invalid login token
	@Test(expected = UserNotInSessionException.class)
	public void noLogin(){
		new Expectations() {{
			remote.storeDocument(anyString, anyString, (byte[]) any); times = 0;
		}};
		
		String bad_session_token = "Bad session token";
		ExportDocument service = new ExportDocument(bad_session_token, CS_ID);
		service.execute();
	}
	
	

	
	//Testing the case of trying to export an existing spread sheet and SD-STORE being unavailable
	@Test(expected = UnavailableServiceException.class)
	public void storeServiceUnavailable(){
		new Expectations() {{
			remote.storeDocument(U_USERNAME, CS_NAME, EXPORTED); times = 1;
			result = new RemoteInvocationException();
		}};
		
		ExportDocument service = new ExportDocument(U_TOKEN, CS_ID);
		service.execute();
	}
	
	//Testing the case of trying to export an existing spread sheet and SD-STORE being unable to store it
	@Test(expected = CannotStoreDocumentException.class)
	public void storeServiceCantStore(){
		new Expectations() {{
			remote.storeDocument(U_USERNAME, CS_NAME, EXPORTED); times = 1;
			result = new CannotStoreDocumentException();
		}};
		
		ExportDocument service = new ExportDocument(U_TOKEN, CS_ID);
		service.execute();
	}
		

	//Testing the case of trying to export an existing spread sheet with a user without permission
	@Test(expected = PermissionException.class)
	public void noPermission(){
		new Expectations() {{
			remote.storeDocument(anyString, anyString, (byte[]) any); times = 0;
		}};
		
		ExportDocument service = new ExportDocument(no_permission_token, CS_ID);
		service.execute();
	}
	
	
	
	}



