package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.CalcSheet;
// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;

public class CreateSpreadSheet extends SessionService {
	 	private CalcSheet result;
	 	private String name;
	 	private int rows;
	 	private int columns;

	 	//constructor
	 	public CreateSpreadSheet(String accessToken, String name, int rows, int columns) {
	 		super(accessToken);
	 		
	 		this.name = name;
	 		this.rows = rows;
	 		this.columns = columns;
	 	}


	 	@Override
	 	protected void afterSuperAction() throws BubbleDocsException {
	 			
	 		result = super.user.createCalcSheet(name, rows, columns);
	 	}


	 	public CalcSheet getResult() {
	 		return result;
	 	}

}
