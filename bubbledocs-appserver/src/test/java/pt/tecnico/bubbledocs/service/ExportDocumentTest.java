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

import pt.tecnico.bubbledocs.dml.Add;
import pt.tecnico.bubbledocs.dml.CalcSheet;
import pt.tecnico.bubbledocs.dml.Cell;
import pt.tecnico.bubbledocs.dml.FunctionArgument;
import pt.tecnico.bubbledocs.dml.Literal;
import pt.tecnico.bubbledocs.dml.LiteralArgument;
import pt.tecnico.bubbledocs.dml.Reference;
import pt.tecnico.bubbledocs.dml.ReferenceArgument;
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
	private int CS_ID;
	private final String CS_NAME = "cs";
	private final int CS_ROWS = 3;
	private final int CS_LINES = 3;
	
	private String CELL_ID0;
	private String CELL_ID1;
	private String CELL_ID2;
	private final int VAL0 = 0;
	private final String LIT0 = "0";
	
	
	@Override
	public void populate4Test(){
		USER = createUser(U_USERNAME, U_PASS, U_NAME);
		U_TOKEN = addUserToSession(U_USERNAME);
		
		CS_EMPTY = createSpreadSheet(USER, CS_NAME, CS_ROWS, CS_LINES);
		CS_ID = CS_EMPTY.getId();
		CELL_ID0 = this.getSpreadSheet(CS_NAME).getCell(1, 1).getId();
		CELL_ID1 = this.getSpreadSheet(CS_NAME).getCell(1, 2).getId();
		CELL_ID2 = this.getSpreadSheet(CS_NAME).getCell(2, 1).getId();
	}
	
	
	@Test
	public void emptyCalcSheet() throws JDOMException, IOException{
		ExportDocument service = new ExportDocument(U_TOKEN, CS_ID);
		service.dispatch();
		
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
	
	//copying a lot of code from above, not sure if i need to repeat the tests above, need to ask
	//the teacher on Tuesday
	@Test
	public void calcSheetWithOneCell() throws JDOMException, IOException{
		Cell c=this.getSpreadSheet(CS_NAME).getCell(CELL_ID0);
		c.setContent(new Literal(7));
		
		ExportDocument service = new ExportDocument(U_TOKEN, CS_ID);
		service.dispatch();
		
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
		assertEquals("literal cell is okey", 7 ,literalElement.getAttribute("val").getIntValue());
		
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
		
		ExportDocument service = new ExportDocument(U_TOKEN, CS_ID);
		service.dispatch();
		
		//a local setup
		SAXBuilder b=new SAXBuilder();
		Document xmlDoc=b.build(new ByteArrayInputStream(service.getDocXML()));
		
        XPathFactory xFactory = XPathFactory.instance();

        XPathExpression<Element> expr = xFactory.compile("/calcSheet", Filters.element());
        List<Element> links = expr.evaluate(xmlDoc);
        Element sheetElement=links.get(0);
        Element literalElement=sheetElement.getChild("literal");
        Element referenceElement=sheetElement.getChild("reference");
        Element pointedLiteralElement=referenceElement.getChild("cell").getChild("literal");
        Element addElement=sheetElement.getChild("add");
        Element arg1Element=sheetElement.getChild("reference");
        Element pointedLiteralElement2=arg1Element.getChild("literal");
        Element arg2Element=sheetElement.getChild("literal");
		
        //asserting the literal cell exists and has the correct value
		assertEquals("literal cell is okey", 7 ,literalElement.getAttribute("val").getIntValue());
		//asserting the reference is pointing to the literal
		assertEquals("reference points to the literal", 7, 
				pointedLiteralElement.getAttribute("val").getIntValue());
		assertEquals("add element has the correct reference and literal", 12, 
				pointedLiteralElement2.getAttribute("val").getIntValue()+arg2Element.getAttribute("val").getIntValue());
		
		sheetElement.getChildren().remove(literalElement);
		sheetElement.getChildren().remove(referenceElement);
		sheetElement.getChildren().remove(addElement);
		//asserting that all the other cells have empty contents
		for(Element cellElement: sheetElement.getChildren()){
			assertEquals("empty cell", 0 ,cellElement.getContentSize());	
		}
		
	}
	
	
	@Test(expected= NotFoundException.class)
	public void nonExistingCalcSheet() throws JDOMException, IOException{
		ExportDocument service = new ExportDocument(U_TOKEN, -1);
		service.dispatch();
		
		}
		
	}
	
	