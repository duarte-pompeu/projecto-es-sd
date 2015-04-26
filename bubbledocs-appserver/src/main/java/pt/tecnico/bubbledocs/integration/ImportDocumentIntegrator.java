package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.service.ImportDocumentService;

public class ImportDocumentIntegrator extends BubbleDocsIntegrator {

	private ImportDocumentService service;
	
	public ImportDocumentIntegrator(String userToken, int docId) {
		service = new ImportDocumentService(userToken, docId);
    }
	
	@Override
	public void execute() throws BubbleDocsException {
		service.execute();
	}
	
    public int getOldDocId(){ 
    	return service.getOldDocId(); 
    }
    
    public int getNewDocId(){ 
    	return service.getNewDocId();     
    }

}
