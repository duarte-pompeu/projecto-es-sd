package pt.tecnico.bubbledocs.service;

// add needed import declarations
import pt.tecnico.bubbledocs.dml.BubbleDocs;
import pt.tecnico.bubbledocs.dml.CalcSheet;
import pt.tecnico.bubbledocs.dml.Cell;
import pt.tecnico.bubbledocs.dml.Literal;
import pt.tecnico.bubbledocs.dml.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.InvalidFormatException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;

public class AssignLiteralCell extends BubbleDocsService{
    private String result;
    private String accessUsername;
    private int docId;
    private String cellId;
    String literal;

    
    //TODO: revert method signature to example signature?
    public AssignLiteralCell(String accessUsername, int docId, String cellId, String literal) {
    	
    	this.accessUsername = accessUsername;
    	this.docId = docId;
    	this.cellId = cellId;
    	this.literal = literal;
    }
    
    // TODO:AssignLiteralCell: finish service
    // TODO:AssignLiteralCell: TEST TEST TEST
    @Override
    public void dispatch() throws BubbleDocsException {
    	Integer literal_val;
    	// check if literal string translates to an int
    	try{
    		literal_val = Integer.valueOf(literal);
    	}
    	catch (NumberFormatException NFEexception){
    		throw new InvalidFormatException(literal + " isnt an integer.");
    	}
    	
    	//TODO: check if token is in session
    	
    	// check if doc exists
    	BubbleDocs bd = BubbleDocs.getInstance();
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
    	
    	// check if cell exists
    	if(!cs.hasCell(cellId)){
    		throw new NotFoundException("can't find cell with ID " + docId + ".");
    	}
    	
    	//TODO: check if cell is protected
    	
    	
    	
    	Literal content = new Literal(literal_val);
    	User user = bd.getUser(accessUsername);
    	
    	// FIXME: AssignLiteralCell: this is a weird way to get a cell line and column
    	// we find the cell, get the object, and then access it again
    	Cell cell = cs.getCell(cellId);
    	cs.setContent(user, content, cell.getLine(), cell.getColumn());
    }

    public String getResult() {
        return result;
    }

}
