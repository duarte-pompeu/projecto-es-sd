package pt.tecnico.bubbledocs.domain;


/**
 *  API to interact with core of BubbleDocs.
 *  Why? To serve as a 'gatekeeper' who manages permissions.
 *  This aims to reduce permission worrying in BubbleDocs core methods.
 *  
 *  As a public API, it makes sense that most or all its callable methods are:
 *  1 - public
 *  2 - static
 *  3 - only return primitive types or copies of objects
 *  	if an actual object is passed, the 'gatekeeper' abstraction is broken, 
 *  	because the services will be able to modify the object and therefore the BubbleDocs core data.
 */
public class API extends APIHelper {
	
	public static String [][] fromCSgetCellsMatrix(String userToken, String sheet){
		User user;
		CalcSheet cs;
		int docID;
		
		user = confirmTokenInSession(userToken);
		docID = confirmParsableToInt(sheet);
		cs = confirmCanRead(user, docID);
		
		return cs.getCellsMatrix();
	}
	
	
	public static String onCSmarkdownPrint(String userToken, String sheet){
		User user;
		CalcSheet cs;
		int docID;
		
		user = confirmTokenInSession(userToken);
		docID = confirmParsableToInt(sheet);
		cs = confirmCanRead(user, docID);
		
		return cs.markdownPrint();
	}


	public static String onCSAssignLiteral(String userToken, int docID, String cellID, String literal){
		User user;
		CalcSheet cs;
		Content cont;
		Cell cell;
		
		user = confirmTokenInSession(userToken);
		cs = confirmCanWrite(user, docID);
		cont = confirmParsableToLiteral(literal);
		cell = fromCSgetCell(cs, cellID);
		
		cell.setContent(cont);
		return cell.getContent().toString();
	}
	
	
	public static String onCSAssignReference(String userToken, int docID, String cellID, String referencedID){
		User user;
		CalcSheet cs;
		Content cont;
		Cell host;
		Cell referenced;
		
		user = confirmTokenInSession(userToken);
		cs = confirmCanWrite(user, docID);
		host = fromCSgetCell(cs, cellID);
		referenced = fromCSgetCell(cs, referencedID);
		cont = new Reference(referenced);
		
		host.setContent(cont);
		//FIXME what if referenced cell content is null?!?!?!
		return host.getContent().toString();
	}
}
