package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.service.ExportDocument;

public class ExportDocumentIntegrator extends BubbleDocsIntegrator {

	private ExportDocument service;
	
	public ExportDocumentIntegrator(String userToken, int docId) {
		service = new ExportDocument(userToken, docId);
    }
	
	@Override
	public void execute() throws BubbleDocsException {
		service.execute();
	}

}
