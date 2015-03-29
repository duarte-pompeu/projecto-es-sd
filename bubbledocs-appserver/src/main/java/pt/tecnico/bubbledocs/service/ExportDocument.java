package pt.tecnico.bubbledocs.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jdom2.output.XMLOutputter;

import pt.tecnico.bubbledocs.BubbleApplication;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.CalcSheet;
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
		
		// FIXME: variable user is not used, considerer removing
		// User user;
		CalcSheet c = null;
    	try{
    		//user = getSessionFromToken(userToken).getUser();
    		getSessionFromToken(userToken).getUser();
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
    	
    	XMLOutputter xmlOut = new XMLOutputter();
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	try {
			xmlOut.output(d, out);
			out.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    	 
    	docXML = out.toByteArray();
    	
    }
}
