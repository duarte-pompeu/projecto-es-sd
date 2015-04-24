package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.API;

public class GetSpreadsheetContentService extends BubbleDocsService {
	private String token;
	private String sheet;
	private String [][] result = null;
	
	
	public GetSpreadsheetContentService(String userToken, String sheetID){
		token = userToken;
		sheet = sheetID;
	}
	
	
	@Override
	public void dispatch(){
		result = API.fromCSgetCellsMatrix(token, sheet);
	}
	
	
	public String[][] getResult(){
		return this.result;
	}
}
