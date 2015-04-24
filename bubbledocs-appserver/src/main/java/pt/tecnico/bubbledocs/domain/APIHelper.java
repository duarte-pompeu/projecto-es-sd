package pt.tecnico.bubbledocs.domain;

import pt.tecnico.bubbledocs.exceptions.InvalidFormatException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;

public class APIHelper {
	
	protected static BubbleDocs getBD(){
		return BubbleDocs.getInstance();
	}
	
	
	protected static User confirmTokenInSession(String token){
		BubbleDocs bd = getBD();
		Session session = bd.getSessionFromToken(token);
		
		if(session == null){
			//throw smth, although it seems getSession already throws
		}
		
		return session.getUser();
	}
	
	
	protected static int confirmParsableToInt(String str) throws InvalidFormatException {
		int parsed;
		
		try{
			parsed = Integer.parseInt(str);
		} catch(NumberFormatException e){
			throw new InvalidFormatException(e.getMessage());
		}
		
		return parsed;
	}
	

	protected static Literal confirmParsableToLiteral(String str) throws InvalidFormatException{
		int value = confirmParsableToInt(str);
		
		return new Literal(value);
	}

	
	protected static CalcSheet confirmCanRead(User user, int docID) {
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
	
	
	protected static Cell fromCSgetCell(CalcSheet cs, String cellID) {
		Cell cell = cs.getCell(cellID);
		
		if(cell == null){
			throw new NotFoundException("");
		}
		
		return cell;
	}

	
	protected static CalcSheet confirmCanWrite(User user, int docID) {
		BubbleDocs bd = getBD();
		CalcSheet cs = bd.getCalcSheetById(docID);
		
		try{
			cs = bd.getCalcSheetById(docID);
		} catch (NotFoundException e){
			throw e;
		}
		
		if(!user.canWrite(cs)){
			throw new PermissionException("User '" + user.getUserName() + "' has not enough"
					+ "privilieges to read '" + cs.getName()+"'.");
		}
		
		return cs;
	}
}
