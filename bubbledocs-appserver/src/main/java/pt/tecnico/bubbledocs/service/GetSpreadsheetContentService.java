package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.InvalidFormatException;
import pt.tecnico.bubbledocs.exceptions.NullContentException;

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
		BubbleDocs bd = BubbleDocs.getInstance();
		int docID;
		
		
		try{
			docID = Integer.parseInt(sheet);
		}
		catch(NumberFormatException e){
			throw new InvalidFormatException(e.getMessage());
		}
		
		User u = getUserFromToken(token);
		result = bd.getCalcSheetById(docID).getCellsMatrix(u);
	}
	
	
	public String[][] getResult(){
		return this.result;
	}
}
