package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.dml.BubbleDocs;
import pt.tecnico.bubbledocs.dml.CalcSheet;
import pt.tecnico.bubbledocs.dml.Cell;
import pt.tecnico.bubbledocs.dml.Literal;
import pt.tecnico.bubbledocs.dml.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.InvalidFormatException;
import pt.tecnico.bubbledocs.exceptions.LoginException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;

public class AssignLiteralCell extends BubbleDocsService{
    private String result;
    private String accessToken;
    private int docId;
    private String cellId;
    String literal;

    public AssignLiteralCell(String accessToken, int docId, String cellId, String literal) {
    	
    	this.accessToken = accessToken;
    	this.docId = docId;
    	this.cellId = cellId;
    	this.literal = literal;
    }
    
    @Override
    public void dispatch() throws InvalidFormatException, NotFoundException, 
    	LoginException, PermissionException {
    	
    	// check if literal string translates to an int
    	Integer literal_val;
    	try{
    		literal_val = Integer.valueOf(literal);
    	}
    	catch (NumberFormatException NFEexception){
    		throw new InvalidFormatException(literal + " isnt an integer.");
    	}
    	
    	// check if token is in session
    	BubbleDocs bd = BubbleDocs.getInstance();
    	User user;
    	try{
    		user = getSessionFromToken(accessToken).getUser();
    	}
    	catch(BubbleDocsException e){
    		throw e;
    	}
    	
    	// check if doc exists	
    	CalcSheet cs = null;
    	for(CalcSheet tempCs: bd.getCalcSheetSet()){
    		if(tempCs.getId() == docId){
    			cs = tempCs;
    		}
    	}
    	
    	if(cs == null){
    		throw new NotFoundException("can't find calcsheet with ID " + docId + ".");
    	}
    	
    	
//    	//TODO: check if user has write access
    	if(!user.canWrite(cs)){
    		throw new PermissionException();
    	}
    	
    	// check if cell exists
    	if(!cs.hasCell(cellId)){
    		throw new NotFoundException("can't find cell with ID " + cellId + ".");
    	}
    	
    	// check if cell is protected
    	Cell cell = cs.getCell(cellId);
    	if(cell.getProtect()){
    		throw new PermissionException();
    	}
    	
    	cs.setContent(user, new Literal(literal_val), cellId);
    }

    public String getResult() {
        return result;
    }
}
