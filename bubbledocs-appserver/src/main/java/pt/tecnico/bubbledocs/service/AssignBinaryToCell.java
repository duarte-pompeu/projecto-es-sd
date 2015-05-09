package pt.tecnico.bubbledocs.service;
//imports
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.domain.BinaryFunction;
import pt.tecnico.bubbledocs.domain.FunctionArgument;
import pt.tecnico.bubbledocs.domain.Content;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.exceptions.InvalidFormatException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssignBinaryToCell extends BubbleDocsService {
	private Content result;
	private String accessToken;
	private int docId;
	private String cellId;
	private String FunctionExp;
	private String Result = "";


	public AssignBinaryToCell(String CellId, String Function, int CsId, String UserToken) {

		this.accessToken = UserToken;
		this.docId = CsId;
		this.cellId = CellId;
		this.FunctionExp = Function;

	}

	@Override
	public void dispatch() throws BubbleDocsException 	{



		// token in session
		BubbleDocs bd = BubbleDocs.getInstance();
		User user;
		try{
			user = getSessionFromToken(accessToken).getUser(); //throws UserNotInSessionException
		}
		catch(UserNotInSessionException e){
			throw e;
		}

		CalcSheet c1 = bd.getCalcSheetById(docId);
		Cell cell = c1.getCell(cellId);


		if (cell == null) throw new NotFoundException("Cell out of bounds in " + cellId);


		CalcSheet cs = null;
		for(CalcSheet tempCs: bd.getCalcSheetSet()){
			if(tempCs.getId() == docId){
				cs = tempCs;
			}
		}

		if(cs == null){
			throw new NotFoundException("can't find calcsheet with ID " + docId + ".");
		}


		//TODO: check if user has write access
		if(!user.canWrite(cs)){
			throw new PermissionException();
		}





		//Lets check the expression

		Pattern p = Pattern.compile("=(ADD|SUB|MUL|DIV)\\(([1-9]+[0-9]*,[1-9]+[0-9]*|[1-9]+[0-9]*);([1-9]+[0-9]*,[1-9]+[0-9]*|[1-9]+[0-9]*)\\)");
		Matcher m = p.matcher(FunctionExp);

		if ( !m.matches() ) {

			throw new InvalidFormatException();

		}		

		//System.out.println("we have a match!!"); 

		BinaryFunction Func2apply = bd.parseNameToBin(FunctionExp.substring(1, 4)) ;


		Content argument4func1, argument4func2 ; 

		String[] parsed = FunctionExp.split(";");

		int openbracket_index = parsed[0].indexOf("(");
		int ending_index = parsed[0].length() ; 
		int comma_index = parsed[0].indexOf(","); 
		//int closebracket_index = parsed[0].indexOf(")");

		if (comma_index != -1) { 

			//System.out.println("this is a ref pointing to " + parsed[0].substring(openbracket_index + 1, comma_index) +"," + parsed[0].substring(comma_index + 1, ending_index) ) ; 
			argument4func1 = new Reference ( new Cell ( Integer.valueOf( parsed[0].substring(openbracket_index + 1, comma_index)) ,  Integer.valueOf(parsed[0].substring(comma_index + 1, ending_index) )    )      ) ; 


		} else {
			//System.out.println("this is a literal : " + parsed[0].substring(openbracket_index + 1, ending_index ));
			argument4func1 =  new Literal( Integer.valueOf( parsed[0].substring(openbracket_index + 1, ending_index ) ) );

		}



		int closebracket_index = parsed[1].indexOf(")");
		comma_index = parsed[1].indexOf(","); 
		//int closebracket_index = parsed[0].indexOf(")");
		if (comma_index != -1) { 

			//System.out.println("this is a ref pointing to " + parsed[1].substring(0, comma_index) +"," + parsed[1].substring(comma_index + 1, closebracket_index ) ) ; 
			argument4func2 = new Reference ( new Cell( Integer.valueOf(parsed[1].substring(0, comma_index)) ,  Integer.valueOf(parsed[1].substring(comma_index + 1, closebracket_index )))) ; 

		} else {
			//System.out.println("this is a literal : " + parsed[1].substring(0, closebracket_index ));
			argument4func2 = new Literal (Integer.valueOf( parsed[1].substring(0, closebracket_index ) ));
		}






		/*

		O metodo init de BinaryFunction recebe FunctionArgument, e argument4func1 e argument4func2 sao Content com o literal/referencia. Convert? 



		Func2apply.init( argument4func1, argument4func2);



		cs.setContent(user, Func2apply, cellId);



		 */
	}

	public final String getResult() {
		return Result;
	}
}
