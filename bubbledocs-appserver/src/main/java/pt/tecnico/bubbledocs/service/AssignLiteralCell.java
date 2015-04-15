package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.User;
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
    	
    	User user = getUserFromToken(accessToken);	
    	CalcSheet cs = BubbleDocs.getInstance().getCalcSheetById(docId);
        cs.setContent(user, new Literal(literal), cellId);
    	result = cs.getContent(user, cellId).toString();
    }

    public String getResult() {
        return result;
    }
}
