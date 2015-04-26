package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.service.AssignLiteralCell;

public class AssignLiteralCellIntegrator extends BubbleDocsIntegrator {

	private AssignLiteralCell service;
	
	public AssignLiteralCellIntegrator(String accessToken, int docId, String cellId, String literal) {
		service = new AssignLiteralCell(accessToken, docId, cellId, literal);
	}
	
	@Override
	public void execute() throws BubbleDocsException {
		service.execute();
	}
	
	public String getResult() {
		return service.getResult();
	}

}
