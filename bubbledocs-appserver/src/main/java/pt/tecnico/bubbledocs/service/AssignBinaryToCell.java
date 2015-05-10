package pt.tecnico.bubbledocs.service;
//imports
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.LiteralArgument;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.ReferenceArgument;
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

	@Override
	public void dispatchAfterSuperService() throws BubbleDocsException 	{
		// token in session
		BubbleDocs bd = BubbleDocs.getInstance();

		this.sheet = bd.getCalcSheetById(docId);		
		
		//Lets check the expression

		Pattern p = Pattern.compile("=(ADD|SUB|MUL|DIV)\\(([1-9]+[0-9]*,[1-9]+[0-9]*|[1-9]+[0-9]*);([1-9]+[0-9]*,[1-9]+[0-9]*|[1-9]+[0-9]*)\\)");
		Matcher m = p.matcher(FunctionExp);

		if ( !m.matches() ) {
			throw new InvalidFormatException();
		}		

		String[] tokens = FunctionExp.split("[=\\(\\),]");
		
		// 0:"" = 1:"ADD" ( 2:"5" , 3:"1;2" )
		String functionName = tokens[1];
		String argument1 = tokens[2];
		String argument2 = tokens[3];

		BinaryFunction functionPrototype = bd.parseNameToBin(functionName);
		
		FunctionArgument arg1 = parseFunctionArgument(argument1);
		FunctionArgument arg2 = parseFunctionArgument(argument2);
		
		functionPrototype.setArgument1(arg1);
		functionPrototype.setArgument2(arg2);
		
		this.sheet.setContent(super.user, functionPrototype, cellId);
		
		try {
			Result = Integer.toString(functionPrototype.getValue());
		} catch (NullContentException e) {
			Result = "#VALUE";
		}
		
	}
	
	private static final Pattern posNumberPattern = Pattern.compile("[1-9](\\d)*");
	private static final Pattern refArgPattern = Pattern.compile(posNumberPattern + ";" + posNumberPattern);
	
	private FunctionArgument parseFunctionArgument(String argument) {
		//Is this a reference?
		if (refArgPattern.matcher(argument).matches()) {
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

	public final String getResult() {
		return Result;
	}
}
