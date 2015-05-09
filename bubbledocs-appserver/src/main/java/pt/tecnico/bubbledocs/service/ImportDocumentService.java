package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.*;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;
// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;

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
    protected void dispatchAfterSuperService() throws BubbleDocsException {   	
    	
    	User user= super.user;
    	String userName = super.user.getUserName();
    	
    	CalcSheet c=BubbleDocs.getInstance().getCalcSheetById(oldDocId);
    	String sheetName=c.getName();
    	String creatorName=c.getCreator().getUserName();
    	
    	//if the token does not belong to the creator of the calcSheet, return an exception
    	if(!creatorName.equals(user.getUserName()))
    		throw new PermissionException();
    	
    	StoreRemoteServices service=new StoreRemoteServices();
		docXML = service.loadDocument(userName, sheetName);
		
		newDocId= BubbleDocs.getInstance().createNewDocument(docXML);
    }
}
