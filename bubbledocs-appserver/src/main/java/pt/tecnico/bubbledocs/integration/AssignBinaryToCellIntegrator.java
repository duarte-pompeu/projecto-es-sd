package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.service.AssignBinaryToCell;

public class AssignBinaryToCellIntegrator extends BubbleDocsIntegrator {

	private AssignBinaryToCell service;
	
	public AssignBinaryToCellIntegrator(String cellId, String expression, int docId, String accessToken) {
		service = new AssignBinaryToCell(cellId, expression, docId, accessToken);
	}
	
	@Override
	public void execute() throws BubbleDocsException {
		service.execute();
	}
	
	public String getResult() {
		return service.getResult();
	}

}
