package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;

public class ImportDocumentService extends BubbleDocsService {
	private byte[] docXML;
    String userToken;
    int oldDocId;
    int newDocId;
    
    public byte[] getDocXML() {
	return docXML;
    }
    
    public int getOldDocId(){ return oldDocId; }
    public int getNewDocId(){ return newDocId; }
    
    public ImportDocumentService(String userToken, int docId) {
    	this.userToken=userToken;
    	this.oldDocId=docId;
    }

    @Override
    protected void dispatch() throws BubbleDocsException {
		User user = getUserFromToken(userToken);    	
		docXML = BubbleDocs.getInstance().loadDocument(user, oldDocId);
		newDocId= BubbleDocs.getInstance().createNewDocument(docXML);
    }
}
