package pt.tecnico.bubbledocs.service;

// add needed import declarations
import pt.tecnico.bubbledocs.dml.BubbleDocs;
import pt.tecnico.bubbledocs.dml.CalcSheet;
import pt.tecnico.bubbledocs.dml.Cell;
import pt.tecnico.bubbledocs.dml.Literal;
import pt.tecnico.bubbledocs.dml.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;

public class AssignLiteralCell extends BubbleDocsService{
    private String result;
    private String accessUsername;
    private int docId;
    private String cellId;
    private String literal;

    // TODO:AssignLiteralCell: finish service
    // TODO:AssignLiteralCell: test
    public AssignLiteralCell(String accessUsername, int docId, String cellId, String literal) {
    	
    	this.accessUsername = accessUsername;
    	this.docId = docId;
    	this.cellId = cellId;
    	this.literal = literal;
    }

    @Override
    protected void dispatch() throws BubbleDocsException {
    	//TODO: make sure it's a literal
    	
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
    	if(!cs.hasCell(Integer.valueOf(cellId))){
    		throw new NotFoundException("can't find cell with ID " + docId + ".");
    	}
    	
    	Literal content = new Literal(Integer.valueOf(literal));
    	User user = bd.getUser(accessUsername);
    	
    	// FIXME: AssignLiteralCell: this is a weird way to get a cell line and column
    	// we find the cell, get the object, and then access it again
    	Cell cell = cs.getCell(Integer.valueOf(cellId));
    	try{
    		cs.setContent(user, content, cell.getLine(), cell.getColumn());
    	}
    	catch (PermissionException permException){	throw permException;	}
    }

    public String getResult() {
        return result;
    }

}
