package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;

public class ExportDocument extends SessionService {
    private byte[] docXML;
    int docId;    
    
    public byte[] getDocXML() {
	return docXML;
    }

    public ExportDocument(String userToken, int docId) {
    	super(userToken);
    	
    	this.docId=docId;
    }

    @Override
    protected void doAfterSuperService() throws BubbleDocsException { 	
		docXML = BubbleDocs.getInstance().storeDocument(super.user, docId);    	
    }
}
