package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.DOMBuilder;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.junit.Test;

import pt.tecnico.bubbledocs.dml.CalcSheet;
import pt.tecnico.bubbledocs.dml.Cell;
import pt.tecnico.bubbledocs.dml.User;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;

public class ExportDocumentTest extends BubbleDocsServiceTest {
	
	private final String U_USERNAME = "jubileu";
	private final String U_PASS = "password";
	private final String U_NAME = "Jubileu Mandafacas";
	private String U_TOKEN;
	
	private User USER;
	
	private CalcSheet CS_EMPTY;
	private int CS_E_ID;
	private final String CS_NAME = "Empty";
	private final int CS_ROWS = 3;
	private final int CS_LINES = 3;
	
	/*
	
	private CalcSheet CS_SHEET;
	private int CS_ID;
	private final String CS_NAME = "Cabulas ES";
	
	
	private String CELL_ID0;
	private final int VAL0 = 0;
	private final String LIT0 = "0";
*/
	
	@Override
	public void populate4Test(){
		USER = createUser(U_USERNAME, U_PASS, U_NAME);
		U_TOKEN = addUserToSession(U_USERNAME);
		
		CS_EMPTY = createSpreadSheet(USER, CS_NAME, CS_ROWS, CS_LINES);
		CS_E_ID = CS_EMPTY.getId();

	}
	
	
	@Test
	public void emptyCalcSheet() throws JDOMException, IOException{
		ExportDocument service = new ExportDocument(U_TOKEN, CS_NAME);
		service.dispatch();
		
		//a local setup
		SAXBuilder b=new SAXBuilder();
		Document xmlDoc=b.build(new ByteArrayInputStream(service.getDocXML()));
		
        XPathFactory xFactory = XPathFactory.instance();

        XPathExpression<Element> expr = xFactory.compile("/", Filters.element());
        List<Element> links = expr.evaluate(xmlDoc);
        Element sheetElement=links.get(0);
      		
		String nameReadFromDocument= sheetElement.getAttribute("creator").getValue();
        int spreadSheetId= sheetElement.getAttribute("Id").getIntValue();
		String spreadSheetDate= sheetElement.getAttribute("date").getValue();
        String spreadSheetName= sheetElement.getAttribute("name").getValue();
        int spreadSheetLines = sheetElement.getAttribute("lines").getIntValue();
        int spreadSheetColumns = sheetElement.getAttribute("columns").getIntValue();
		
		//asserting all the attribute values
		assertEquals("Owner is correct", U_USERNAME, nameReadFromDocument);
		assertEquals("ID is correct", this.getSpreadSheet(CS_NAME).getId().intValue(), spreadSheetId );
		assertEquals("Creation date is correct", 
				this.getSpreadSheet(CS_NAME).getDate().toString(), spreadSheetDate );
		assertEquals("Name is correct", this.getSpreadSheet(CS_NAME).getName(), spreadSheetName );
		assertEquals("The number of lines is correct", 
				this.getSpreadSheet(CS_NAME).getLines().intValue(), spreadSheetLines );
		assertEquals("The number of columns is correct", 
				this.getSpreadSheet(CS_NAME).getColumns().intValue(), spreadSheetColumns );
		
		//asserting the correct number of cells
		assertEquals("The number of cells is correct", 9, sheetElement.getChildren().size());
		
		//asserting that all the cells have null contents
		for(Element cellElement: sheetElement.getChildren()){
			assertEquals("empty cell", 0 ,cellElement.getContentSize());	
		}
		
	}
	/*
	
	@Test(expected = NotFoundException.class)
	public void cellDoesntExist(){
	
	}
	
	
	@Test(expected = NotFoundException.class)
	public void cellOutOfBonds(){
	
		
	
		
	}
	
	
	@Test(expected = NotFoundException.class)
	public void docDoesntExist(){
	
	}
	
	
	//FIXME: do we even have methods to protect cells?
	@Test(expected = PermissionException.class)
	public void cellIsProtected(){
	
	
	}
	
	
	@Test(expected = PermissionException.class)
	public void noWriteAccess(){
	
		
	}*/
}
