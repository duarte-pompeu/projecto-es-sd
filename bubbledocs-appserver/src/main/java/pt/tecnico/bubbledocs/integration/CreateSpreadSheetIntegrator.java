package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.service.CreateSpreadSheet;

public class CreateSpreadSheetIntegrator extends BubbleDocsIntegrator {
	
	private CreateSpreadSheet service;
	
	public CreateSpreadSheetIntegrator(String accessToken, String name, int rows, int columns) {
		service = new CreateSpreadSheet(accessToken, name, rows, columns);
	}
		
	@Override
	public void execute() throws BubbleDocsException {
		service.execute();
	}
	
	public CalcSheet getResult() {
		return service.getResult();
	}
}
