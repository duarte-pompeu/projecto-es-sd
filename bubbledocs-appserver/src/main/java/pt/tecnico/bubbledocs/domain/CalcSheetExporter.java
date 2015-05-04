package pt.tecnico.bubbledocs.domain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.ist.fenixframework.FenixFramework;

public class CalcSheetExporter {
	
	//This can easily be replaced with a stack in a more complex case
	private Element contentElement;
	private Element argumentElement;
	
	public byte[] exportToXmlData(CalcSheet sheet) {
		return this.exportToXmlData(this.exportToXml(sheet));
	}
	
	public byte[] exportToXmlData(Document document) {
		return this.exportToXmlData(document, Format.getRawFormat());
	}
	
	public byte[] exportToPrettyXmlData(CalcSheet sheet) {
		return this.exportToPrettyXmlData(this.exportToXml(sheet));
	}
	
	public byte[] exportToPrettyXmlData(Document document) {
		return this.exportToXmlData(document, Format.getPrettyFormat());
	}
	
	private byte[] exportToXmlData(Document document, Format format) {
		XMLOutputter xmlOut = new XMLOutputter(format);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			xmlOut.output(document, out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}
	
	
	/*
	 * <calcSheet creator="alice" date="2015-05-01" id="123" name="Cábulas" lines="300" columns="10">
	 *   <cell line="1" column="1" protect="false">
	 *     ...
	 *   </cell>
	 *   <cell line="1" column="2"> <!-- order is dependent how is stored in FF -->
	 *   ...
	 * </calcSheet>
	 */
	
	public Document exportToXml(CalcSheet sheet) {
		Element root = new Element("calcSheet");
		try{
			root.setAttribute("creator", sheet.getCreator().getUserName());
			root.setAttribute("date",    sheet.getDate().toString());
			root.setAttribute("id",      sheet.getId().toString());
			root.setAttribute("name",    sheet.getName().toString());
			root.setAttribute("lines",   sheet.getLines().toString());
			root.setAttribute("columns", sheet.getColumns().toString());

		}catch(Exception e){System.out.println(e.toString());}

		for(Cell c: sheet.getCellSet()){
			if(c.getContent()!=null)
				root.addContent(this.exportCell(c));
		}

		Document document = new Document();
		document.setRootElement(root);
		
		return document;
	}
	
	
	private Element exportCell(Cell cell) {
		return  new Element("cell")
		            .setAttribute("line",    cell.getLine().toString())
		            .setAttribute("column",  cell.getColumn().toString())
		            .setAttribute("protect", cell.getProtect().toString())
		            .addContent(this.exportContent(cell.getContent()));		
	}

	private Element exportContent(Content content) {
		content.accept(this);
		return contentElement;
	}
	
	/*
	 * <cell line="1" column="1" protect="false">
	 *   <literal val="14"></literal>
	 * </cell>
	 *      
	 */
	
	void exportLiteral(Literal content) {
		this.contentElement = 
				new Element("literal")
		            .setAttribute("val", Integer.toString(content.getValue()));
	}
	
	/*
	 * <cell line="1" column="2" protect="false">
	 *   <reference line="1" column="1">
	 * </cell>
	 */
	void exportReference(Reference content) {
		this.contentElement = 
				new Element("reference")
		            .setAttribute("line", content.getPointedCell().getLine().toString())
		            .setAttribute("column", content.getPointedCell().getColumn().toString());
	}
	
	/*
	 * <cell line="1" column="3" protect="false">
	 *   <add>
	 *     <literal val="5"></literal>
	 *     <reference line="1" column="2"></reference>
	 *   </add>
	 * </cell>
	 */
	
	void exportBinaryFunction(BinaryFunction content) {
		this.contentElement = 
				new Element(content.getName())
		            .addContent(this.exportFunctionArgument(content.getArg1()))
		            .addContent(this.exportFunctionArgument(content.getArg2()));		
	}
		
	private Element exportFunctionArgument(FunctionArgument argument) {
		argument.accept(this);
		return argumentElement;
	}
	
	void exportLiteralArgument(LiteralArgument argument) {
		this.argumentElement =
				new Element("literal")
		            .setAttribute("val", Integer.toString(argument.getValue()));
	}
	
	void exportReferenceArgument(ReferenceArgument argument) {
		this.argumentElement =
				new Element("reference")
		            .setAttribute("line", argument.getPointedCell().getLine().toString())
                    .setAttribute("column", argument.getPointedCell().getColumn().toString());
	}
	
	/*
	 * <cell = line="2" column="1" protect="false">
	 *   <prd>
	 *     <range>
	 *       <left-up line="1" column="1"></left-up>
	 *       <right-down line="1" column="3"></right-down>
	 *     </range>
	 *   </prd>
	 * </cell>
	 */
	
	void exportRangeFunction(RangeFunction content) {
		this.contentElement = 
				new Element(content.getName())
		            .addContent(this.exportRange(content.getRange()));		
	}
	
	private Element exportRange(Range range) {
		return new Element("range")
				   .addContent(this.exportRangePart("left-up", range.getLeftUpCell()))
				   .addContent(this.exportRangePart("right-down", range.getRightDownCell()));
	}

	private Element exportRangePart(String name, Cell cell) {
		return new Element(name)
				   .setAttribute("line", cell.getLine().toString())
				   .setAttribute("column",cell.getColumn().toString());
	}
	
	public static void main(String[] args) throws Exception {
		
		FenixFramework.getTransactionManager().begin(false);
		
		/*start transaction*/ {
			BubbleDocs bd = BubbleDocs.getInstance();
			User alice = bd.addUser("alice", "Alice", "alice@example.com", "doNotLookEve");
			CalcSheet sheet = alice.createCalcSheet("Cábulas", 300, 10);
			
			// Vamos escrever umas cenas
			
			sheet.getCell(1,1).setContent(new Literal(14));
			sheet.getCell(1,2).setContent(new Reference(sheet.getCell(1,1)));
			sheet.getCell(1,3).setContent(new Add(new LiteralArgument(5),
					                              new ReferenceArgument(sheet.getCell(1, 2))));
			sheet.getCell(2,1).setContent(new Prd(new Range(1,1,1,3,sheet)));
			
			CalcSheetExporter exporter = new CalcSheetExporter();
			System.out.write(exporter.exportToPrettyXmlData(sheet));
			
		} /*cancel transaction*/
		
		FenixFramework.getTransactionManager().rollback();
	}
	
	
	
}
