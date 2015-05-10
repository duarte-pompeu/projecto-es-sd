package pt.tecnico.bubbledocs.service;
//imports
import pt.tecnico.bubbledocs.domain.Add;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Div;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.LiteralArgument;
import pt.tecnico.bubbledocs.domain.Mul;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.ReferenceArgument;
import pt.tecnico.bubbledocs.domain.Sub;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.domain.BinaryFunction;
import pt.tecnico.bubbledocs.domain.FunctionArgument;
import pt.tecnico.bubbledocs.domain.Content;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.NullContentException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.exceptions.InvalidFormatException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssignBinaryToCell extends SessionService {
	private Content result;
	private String accessToken;
	private int docId;
	private String cellId;
	private String FunctionExp;
	private String Result = "";
	private CalcSheet sheet;


	public AssignBinaryToCell(String CellId, String Function, int CsId, String UserToken) {
		super(UserToken);
		this.docId = CsId;
		this.cellId = CellId;
		this.FunctionExp = Function;

	}

	private static final String OPERATION = "(ADD|SUB|MUL|DIV)";
	private static final String POSITIVE_NUM = "([1-9][0-9]*)";
	private static final String LITERAL = "(0|([+-]?[1-9][0-9]*))";
	private static final String REFERENCE = "(" + POSITIVE_NUM + ";" + POSITIVE_NUM + ")";
	private static final String ARGUMENT = "(" + LITERAL + "|" + REFERENCE + ")";
	private static final String EXPRESSION = "=" + OPERATION + "\\(" + ARGUMENT + "," + ARGUMENT + "\\)";
	
	
	
	@Override
	public void dispatchAfterSuperService() throws BubbleDocsException 	{
		this.sheet = BubbleDocs.getInstance().getCalcSheetById(docId);			
		
		BinaryFunction functionPrototype = parse();		
		this.sheet.setContent(super.user, functionPrototype, cellId);		
		try {
			Result = Integer.toString(functionPrototype.getValue());
		} catch (NullContentException e) {
			Result = "#VALUE";
		}
		
	}

	private BinaryFunction parse() {
		//Lets check the expression

		Pattern p = Pattern.compile(EXPRESSION);
		Matcher m = p.matcher(FunctionExp);

		if ( !m.matches() ) {
			throw new InvalidFormatException();
		}		

		String[] tokens = FunctionExp.split("[=\\(\\),]");
		
		// 0:"" = 1:"ADD" ( 2:"5" , 3:"1;2" )
		String functionName = tokens[1];
		String argument1 = tokens[2];
		String argument2 = tokens[3];

		BinaryFunction functionPrototype = parseNameToBin(functionName);
		
		FunctionArgument arg1 = parseFunctionArgument(argument1);
		FunctionArgument arg2 = parseFunctionArgument(argument2);
		
		functionPrototype.setArgument1(arg1);
		functionPrototype.setArgument2(arg2);
		return functionPrototype;
	}
		
	//The argument happens to have the same expression as the reference
	private static final String REFERENCE_ARG = REFERENCE;
	
	private FunctionArgument parseFunctionArgument(String argument) {
		//Is this a reference?
		if (Pattern.compile(REFERENCE_ARG).matcher(argument).matches()) {
			return new ReferenceArgument(this.sheet.getCell(argument));
		//Welp, maybe a measly Literal.
		} else {
			try {
				int val = Integer.parseInt(argument);
				return new LiteralArgument(val);
			} catch (NumberFormatException e) {
				throw new InvalidFormatException();
			}
		}
	}	
	
	 private BinaryFunction parseNameToBin(String name) {
		switch(name){
			case "ADD": return new Add();
			case "SUB": return new Sub();
			case "MUL": return new Mul();
			case "DIV": return new Div();
			default: return null;
		}	
	}

	public final String getResult() {
		return Result;
	}
}
