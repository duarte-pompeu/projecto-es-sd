package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.dml.BubbleDocs;
import pt.tecnico.bubbledocs.BubbleApplication;
import pt.tecnico.bubbledocs.dml.CalcSheet;
import pt.tecnico.bubbledocs.dml.User;
// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;

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
    	org.jdom2.Document d=null;
		BubbleDocs pb = BubbleDocs.getInstance();
		User user;
		CalcSheet c = null;
    	try{
    		user = getSessionFromToken(userToken).getUser();
    	}catch(BubbleDocsException e){
    		System.out.println(e.toString()+e.getMessage());
    	}
    	
    	for(CalcSheet s : pb.getCalcSheetSet()){
    		if(s.getId()==docId){
    			c=s;
    			break;
    		}
    	}
    	
    	if(c==null)
    		throw new NotFoundException();
    	d=BubbleApplication.convertToXML(c);
    	
    	
    }
}
