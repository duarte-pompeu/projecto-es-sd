package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.service.GetSpreadsheetContentService;

public class GetSpreadsheetContentIntegrator extends BubbleDocsIntegrator {

	private GetSpreadsheetContentService service;
	
	public GetSpreadsheetContentIntegrator(String userToken, String sheetID){
		service = new GetSpreadsheetContentService(userToken, sheetID);
	}
	
	@Override
	public void execute() throws BubbleDocsException {
		service.execute();
	}
	
	public String[][] getResult(){
		return service.getResult();
	}

}
