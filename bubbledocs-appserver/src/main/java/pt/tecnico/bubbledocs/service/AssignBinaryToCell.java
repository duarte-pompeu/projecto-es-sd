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
		CalcSheet sheet = BubbleDocs.getInstance().getCalcSheetById(docId);	
		
		ParseBinaryFunction parse = new ParseBinaryFunction(sheet, FunctionExp);
		parse.execute();
		
		BinaryFunction function = parse.getResult();
				
		sheet.setContent(super.user, function, cellId);		
		
		try {
			Result = Integer.toString(function.getValue());
		} catch (NullContentException e) {
			Result = "#VALUE";
		}
		
	}

	public final String getResult() {
		return Result;
	}
}
