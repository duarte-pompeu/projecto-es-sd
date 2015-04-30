package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.API;

public class GetSpreadsheetContentService extends SessionService {
	private String sheet;
	private String [][] result = null;
	
	
	public GetSpreadsheetContentService(String userToken, String sheetID){
		super(userToken);
		sheet = sheetID;
	}
	
	
	@Override
	public void dispatchAfterSuperService(){
		result = API.fromCSgetCellsMatrix(token, sheet);
	}
	
	
	public String[][] getResult(){
		return this.result;
	}
}
