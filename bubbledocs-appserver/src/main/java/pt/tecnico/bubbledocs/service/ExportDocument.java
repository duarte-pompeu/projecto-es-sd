package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;

public class ExportDocument extends BubbleDocsService {
    private byte[] docXML;
    String userToken;
    int docId;    
    
    public byte[] getDocXML() {
	return docXML;
    }

    public ExportDocument(String userToken, int docId) {
    	this.userToken=userToken;
    	this.docId=docId;
    }

    @Override
    protected void dispatch() throws BubbleDocsException {
		User user = getUserFromToken(userToken);    	
		docXML = BubbleDocs.getInstance().storeDocument(user, docId);    	
    }
}
