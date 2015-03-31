package pt.tecnico.bubbledocs.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jdom2.output.XMLOutputter;

import pt.tecnico.bubbledocs.BubbleApplication;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.User;
// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;

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
		User user=null;
		String userName;
		CalcSheet c = null;
		StoreRemoteServices remoteService=new StoreRemoteServices();
		
		//getting the user from the token
    	try{
    		user = getSessionFromToken(userToken).getUser();
    		getSessionFromToken(userToken).getUser();
    	}catch(BubbleDocsException e){
    		System.out.println(e.toString()+e.getMessage());
    	}
    	//getting the username of the calcsheet owner
    	userName=user.getUserName();
    	
    	for(CalcSheet s : pb.getCalcSheetSet()){
    		if(s.getId()==docId){
    			c=s;
    			break;
    		}
    	}
    	
    	//Converting the calcsheet to a jdom doc and then to a byte array
    	if(c==null)
    		throw new NotFoundException();
    	d=BubbleApplication.convertToXML(c);
    	
    	XMLOutputter xmlOut = new XMLOutputter();
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	try {
			xmlOut.output(d, out);
			out.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    	 
    	docXML = out.toByteArray();
    	
    	remoteService.storeDocument(userName, c.getName(), docXML);
    	
    	
    }
}
