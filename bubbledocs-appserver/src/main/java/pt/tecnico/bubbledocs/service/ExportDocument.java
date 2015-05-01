package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.CalcSheetExporter;
// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;

public class ExportDocument extends SessionService {
    private byte[] docXML;
    int docId;    
    
    public byte[] getResult() {
    	return docXML;
    }

    public ExportDocument(String userToken, int docId) {
    	super(userToken);
    	
    	this.docId=docId;
    }

    @Override
    protected void dispatchAfterSuperService() throws BubbleDocsException { 	
		CalcSheet sheet = BubbleDocs.getInstance().getCalcSheetById(docId);
    	if (!super.user.canWrite(sheet)) throw new PermissionException();
    	docXML = new CalcSheetExporter().exportToXmlData(sheet);    	
    }
    
    public String getUsername() {
    	return super.user.getUserName();
    }
    
    public String getDocumentName() {
    	return BubbleDocs.getInstance().getCalcSheetById(docId).getName();
    }
}
