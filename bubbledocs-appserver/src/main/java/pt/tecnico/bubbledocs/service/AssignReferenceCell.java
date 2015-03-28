package pt.tecnico.bubbledocs.service;
//imports
import pt.tecnico.bubbledocs.dml.BubbleDocs;
import pt.tecnico.bubbledocs.dml.CalcSheet;
import pt.tecnico.bubbledocs.dml.Cell;
import pt.tecnico.bubbledocs.dml.Reference;
import pt.tecnico.bubbledocs.dml.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException; //token in session?
import pt.tecnico.bubbledocs.exceptions.InvalidFormatException; //Is it a Reference?
import pt.tecnico.bubbledocs.exceptions.NotFoundException; //incorrect Cell or Reference given
import pt.tecnico.bubbledocs.exceptions.PermissionException; //User doesnt have write permissions

public class AssignReferenceCell extends BubbleDocsService {
    private String result;
    private String accessToken;
    private int docId;
    private String cellId;
    private String refId;

    public AssignReferenceCell(String tokenUser, int docId, String cellId, String reference) {
		this.accessToken = tokenUser;
		this.docId = docId;
		this.cellId = cellId;
		this.refId = reference;
			
    }

    @Override
    protected void dispatch() throws InvalidFormatException, NotFoundException, PermissionException {
		//token in session
    	BubbleDocs bd = BubbleDocs.getInstance();
    	User user;
    	try{
    		user = getSessionFromToken(accessToken).getUser();
    	}
    	catch(BubbleDocsException e){
    		throw e;
    	}
		
		//check if CalcSheet exists	
    	CalcSheet c1 = null;
    	for(CalcSheet cx: bd.getCalcSheetSet()){
    		if(cx.getId() == docId){
    			c1 = cx;
    		}
    	}
    	if(c1 == null){
    		throw new NotFoundException("Can't find calcsheet with ID " + docId + ".");
    	}
		
		//check if cell exists
    	if(!c1.hasCell(cellId)){
    		throw new NotFoundException("Can't find cell with ID " + cellId + ".");
    	}
		
		//check if reference is valid
		if(!c1.hasCell(refId)){
    		throw new NotFoundException("Can't find reference, cell with ID" + refId + "does not exist.");
    	}
		
		//check if cell is protected
    	Cell cell = c1.getCell(cellId);
    	if(cell.getProtect()){
    		throw new PermissionException();
    	}
		c1.setContent(user, new Reference(c1.getCell(refId)), cellId);
		
    }

    public final String getResult() {
        return result;
    }
}
