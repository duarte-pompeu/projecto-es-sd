package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.service.AssignReferenceCell;

public class AssignReferenceCellIntegrator extends BubbleDocsIntegrator {
	
	private AssignReferenceCell service;
	
	public AssignReferenceCellIntegrator(String tokenUser, int docID, String cellID, String reference) {
		service = new AssignReferenceCell(tokenUser, docID, cellID, reference);
	}	
	
	@Override
	public void execute() throws BubbleDocsException {
		service.execute();
	}
	
	public String getResult() {
		return service.getResult();
	}

}
