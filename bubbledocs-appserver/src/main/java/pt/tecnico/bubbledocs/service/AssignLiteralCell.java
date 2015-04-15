package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Content;
import pt.tecnico.bubbledocs.exceptions.InvalidFormatException;
import pt.tecnico.bubbledocs.exceptions.LoginException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;

public class AssignLiteralCell extends BubbleDocsService{
    private String result;
    private String accessToken;
    private int docID;
    private String cellID;
    String literal;
    
    public AssignLiteralCell(String accessToken, int docID, String cellID, String literal) {
    	
    	this.accessToken = accessToken;
    	this.docID = docID;
    	this.cellID = cellID;
    	this.literal = literal;
    }
    
    @Override
    public void dispatch() throws InvalidFormatException, NotFoundException, 
    	LoginException, PermissionException {
    	
    	// parse string to integer
    	Integer literal_val;
    	try{
    		literal_val = Integer.valueOf(literal);
    	}
    	catch (NumberFormatException NFEexception){
    		throw new InvalidFormatException(literal + " isnt an integer.");
    	}
    	
    	
    	BubbleDocs bd = BubbleDocs.getInstance();
    	Content content = bd.assignLiteral(accessToken, docID, cellID, literal_val);
    	this.result = content.toString();
    }

    public String getResult() {
        return result;
    }
}
