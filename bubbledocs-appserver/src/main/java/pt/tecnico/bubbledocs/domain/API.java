package pt.tecnico.bubbledocs.domain;

import pt.tecnico.bubbledocs.exceptions.InvalidFormatException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;

/**
 *  API to interact with core of BubbleDocs.
 *  Why? To serve as a 'gatekeeper' who manages permissions.
 *  This aims to reduce permission worrying in BubbleDocs core methods.
 *  
 *  As a public API, it makes sense that most or all its methods are:
 *  1 - public
 *  2 - static
 *  3 - only return primitive types or copies of objects
 *  	if an actual object is passed, the 'gatekeeper' abstraction is broken, 
 *  	because the services will be able to modify the object and therefore the BubbleDocs core data.
 */
public class API {
	
	private static BubbleDocs getBD(){
		return BubbleDocs.getInstance();
	}
	
	private static User confirmTokenInSession(String token){
		BubbleDocs bd = getBD();
		Session session = bd.getSessionFromToken(token);
		
		if(session == null){
			//throw smth maybe
		}
		
		return session.getUser();
	}
	
	private static int confirmParsableToInt(String str) throws InvalidFormatException {
		int parsed;
		try{
			parsed = Integer.parseInt(str);
		} catch(NumberFormatException e){
			throw new InvalidFormatException(e.getMessage());
		}
		
		return parsed;
	}
	
	private static CalcSheet confirmCanRead(User user, int docID) {
		BubbleDocs bd = getBD();
		CalcSheet cs = bd.getCalcSheetById(docID);
		
		try{
			cs = bd.getCalcSheetById(docID);
		} catch (NotFoundException e){
			throw e;
		}
		
		if(!user.canRead(cs)){
			throw new PermissionException("User '" + user.getUserName() + "' has not enough"
					+ "privilieges to read '" + cs.getName()+"'.");
		}
		
		return cs;
	}
	
	
	public static String [][] fromCSgetCellsMatrix(String userToken, String sheet){
		User user;
		CalcSheet cs;
		int docID;
		
		user = confirmTokenInSession(userToken);
		docID = confirmParsableToInt(sheet);
		cs = confirmCanRead(user, docID);
		
		return cs.getCellsMatrix();
	}
	
	
	public static String markdownPrint(String userToken, String sheet){
		User user;
		CalcSheet cs;
		int docID;
		
		user = confirmTokenInSession(userToken);
		docID = confirmParsableToInt(sheet);
		cs = confirmCanRead(user, docID);
		
		return cs.markdownPrint();
	}
}
