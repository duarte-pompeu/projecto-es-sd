package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.API;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;

public class AssignLiteralCell extends SessionService{
    private String result;
    private int docId;
    private String cellId;
    String literal;
    
    public AssignLiteralCell(String accessToken, int docId, String cellId, String literal) {
    	super(accessToken);
    	
    	this.docId = docId;
    	this.cellId = cellId;
    	this.literal = literal;
    }
    
    @Override
    protected void dispatchAfterSuperService() throws BubbleDocsException {
    	result = API.onCSAssignLiteral(super.token, docId, cellId, literal);
    }
    

    public String getResult() {
        return result;
    }
}
