package pt.tecnico.bubbledocs.service;

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
		
	}
	
	
	public String[][] getResult(){
		return this.result;
	}
}
