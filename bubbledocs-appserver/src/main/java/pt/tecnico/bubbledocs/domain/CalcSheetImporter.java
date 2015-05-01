package pt.tecnico.bubbledocs.domain;

import org.jdom2.Document;
import org.jdom2.Element;
import org.joda.time.LocalDate;

public class CalcSheetImporter {
	
	CalcSheet sheet;
	
	private void validate(String str1, String str2) {
		if (!str1.equals(str2)) {
			throw new RuntimeException("Invalid XML");
		}
	}
	
	private void validateNumberOfChildren(Element element, int total) {
		int num = element.getChildren().size();
		if (num != total) {
			throw new RuntimeException("Too much children");
		}
	}
	
	private void validateNumberOfChildren(Element element) {
		validateNumberOfChildren(element, 1);
	}
	
	public CalcSheet importFromXml(byte[] data) {
		return importFromXml((Document) null);
	}
	
	public CalcSheet importFromXml(Document document) {
		this.sheet = new CalcSheet();
		Element root = document.getRootElement();
		//validate
		validate(root.getName(), "calcSheet");
		
		//get attributes
		String name = root.getAttributeValue("name");
		String creatorName = root.getAttributeValue("creator");
		User creator = BubbleDocs.getInstance().getUser(creatorName);
		LocalDate date = new LocalDate(root.getAttributeValue("date"));
		int id = Integer.parseInt(root.getAttributeValue("id"));
		int lines = Integer.parseInt(root.getAttributeValue("lines"));
		int columns = Integer.parseInt(root.getAttributeValue("columns"));
		
		//initialize sheet
		this.sheet.init(name, lines, columns);
		this.sheet.setId(id);
		this.sheet.setCreator(creator);
		this.sheet.setDate(date);
		
		//import cells
		
		for (Element cellElement : root.getChildren()) {
			validate(cellElement.getName(), "cell");
			validateNumberOfChildren(cellElement);
			
			int line = Integer.parseInt(cellElement.getAttributeValue("line"));
			int column = Integer.parseInt(cellElement.getAttributeValue("column"));
			boolean protect = Boolean.parseBoolean(cellElement.getAttributeValue("protected"));
			
			Cell cell = this.sheet.getCell(line, column);
			cell.setProtect(protect);
			
			Content content = this.importContent(cellElement.getChildren().get(0));	
		}
		
		
		return this.sheet;
	}
	
	private Content importContent(Element contentElement) {
		switch(contentElement.getName()) {
		case "literal": return importLiteral(contentElement);
		case "reference": return importReference(contentElement);
		case "sub": return importBinaryFunction(contentElement, new Sub());
		case "add": return importBinaryFunction(contentElement, new Add());
		case "mul": return importBinaryFunction(contentElement, new Mul());
		case "div": return importBinaryFunction(contentElement, new Div());
		case "avg": return importRangeFunction(contentElement, new Avg());
		case "prd": return importRangeFunction(contentElement, new Prd());
		default: return null;
		}
	}
	
	private Content importLiteral(Element contentElement) {
		validateNumberOfChildren(contentElement, 0);
		return new Literal(contentElement.getAttributeValue("val"));
	}

	private Content importReference(Element contentElement) {
		validateNumberOfChildren(contentElement, 0);
		int line = Integer.parseInt(contentElement.getAttributeValue("line"));
		int column = Integer.parseInt(contentElement.getAttributeValue("column"));
		Cell pointed = this.sheet.getCell(line, column);
		return new Reference(pointed);
	}
	
	private Content importBinaryFunction(Element contentElement, BinaryFunction prototype) {
		validateNumberOfChildren(contentElement, 2);
		prototype.setArgument1(this.importArgument(contentElement.getChildren().get(0)));
		prototype.setArgument1(this.importArgument(contentElement.getChildren().get(1)));
		
		return prototype;
	}
	
	private FunctionArgument importArgument(Element argumentElement) {
		switch(argumentElement.getName()) {
		case "literal": return importLiteralArgument(argumentElement);
		case "reference": return importReferenceArgument(argumentElement);
		default: return null;
		}
	}

	private LiteralArgument importLiteralArgument(Element argumentElement) {
		validateNumberOfChildren(argumentElement, 0);
		return new LiteralArgument(Integer.parseInt(argumentElement.getAttributeValue("val")));
	}
	
	private ReferenceArgument importReferenceArgument(Element argumentElement) {
		validateNumberOfChildren(argumentElement, 0);
		int line = Integer.parseInt(argumentElement.getAttributeValue("line"));
		int column = Integer.parseInt(argumentElement.getAttributeValue("column"));
		Cell pointed = this.sheet.getCell(line, column);
		return new ReferenceArgument(pointed);
	}

	private Content importRangeFunction(Element contentElement, RangeFunction prototype) {
		validateNumberOfChildren(contentElement);
		Element rangeElement = contentElement.getChild("range");
		validateNumberOfChildren(rangeElement, 2);
		int fromline = Integer.parseInt(rangeElement.getChild("left-up").getAttributeValue("line"));
		int fromcol  = Integer.parseInt(rangeElement.getChild("left-up").getAttributeValue("column"));
		int toline   = Integer.parseInt(rangeElement.getChild("right-down").getAttributeValue("line"));
		int tocol    = Integer.parseInt(rangeElement.getChild("right-down").getAttributeValue("column"));
		prototype.setRange(new Range(fromline, fromcol, toline, tocol, this.sheet));
		
		return prototype;
		
	}

	
}
