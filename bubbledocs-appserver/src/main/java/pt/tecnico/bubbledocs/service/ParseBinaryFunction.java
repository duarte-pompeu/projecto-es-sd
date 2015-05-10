package pt.tecnico.bubbledocs.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.tecnico.bubbledocs.domain.Add;
import pt.tecnico.bubbledocs.domain.BinaryFunction;
import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.Div;
import pt.tecnico.bubbledocs.domain.FunctionArgument;
import pt.tecnico.bubbledocs.domain.LiteralArgument;
import pt.tecnico.bubbledocs.domain.Mul;
import pt.tecnico.bubbledocs.domain.ReferenceArgument;
import pt.tecnico.bubbledocs.domain.Sub;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.InvalidFormatException;

public class ParseBinaryFunction extends BubbleDocsService {

	private CalcSheet sheet;
	private String funcExpression;
	private BinaryFunction result;
	
	private static final String OPERATION = "(ADD|SUB|MUL|DIV)";
	private static final String POSITIVE_NUM = "([1-9][0-9]*)";
	private static final String LITERAL = "(0|([+-]?[1-9][0-9]*))";
	private static final String REFERENCE = "(" + POSITIVE_NUM + ";" + POSITIVE_NUM + ")";
	private static final String ARGUMENT = "(" + LITERAL + "|" + REFERENCE + ")";
	private static final String EXPRESSION = "=" + OPERATION + "\\(" + ARGUMENT + "," + ARGUMENT + "\\)";
	
	public ParseBinaryFunction(CalcSheet sheet, String funcExpression) {
		this.sheet = sheet;
		this.funcExpression = funcExpression;
	}
	
	@Override
	protected void dispatch() throws BubbleDocsException {
		Pattern p = Pattern.compile(EXPRESSION);
		Matcher m = p.matcher(funcExpression);

		if ( !m.matches() ) {
			throw new InvalidFormatException();
		}		

		String[] tokens = funcExpression.split("[=\\(\\),]");
		
		// 0:"" = 1:"ADD" ( 2:"5" , 3:"1;2" )
		String functionName = tokens[1];
		String argument1 = tokens[2];
		String argument2 = tokens[3];

		BinaryFunction functionPrototype = parseNameToBin(functionName);
		
		FunctionArgument arg1 = parseFunctionArgument(argument1);
		FunctionArgument arg2 = parseFunctionArgument(argument2);

		functionPrototype.setArgument1(arg1);
		functionPrototype.setArgument2(arg2);
		
		this.result = functionPrototype;
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


	public BinaryFunction getResult() {
		return result;
	}

}
