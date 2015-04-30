package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;

public class ImportDocumentService extends SessionService {
	private byte[] docXML;
    int oldDocId;
    int newDocId;
    
    public byte[] getDocXML() {
	return docXML;
    }
    
    public int getOldDocId(){ return oldDocId; }
    public int getNewDocId(){ return newDocId; }
    
    public ImportDocumentService(String userToken, int docId) {
    	super(userToken);
    	
    	this.oldDocId=docId;
    }

    @Override
    protected void doAfterSuperService() throws BubbleDocsException {   	
		docXML = BubbleDocs.getInstance().loadDocument(user, oldDocId);
		newDocId= BubbleDocs.getInstance().createNewDocument(docXML);
    }
}
