package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.User;
// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.InvalidFormatException;
import pt.tecnico.bubbledocs.exceptions.InvalidValueException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;

public class CreateSpreadSheet extends BubbleDocsService {
	 	private CalcSheet result;
	 	private String accessToken;
	 	private String name;
	 	private int rows;
	 	private int columns;

	 	//constructor
	 	public CreateSpreadSheet(String accessToken, String name, int rows, int columns) {
	 		this.accessToken = accessToken;
	 		this.name = name;
	 		this.rows = rows;
	 		this.columns = columns;
	 	}


	 	@Override
	 	protected void dispatch() throws BubbleDocsException,UserNotInSessionException,InvalidFormatException, InvalidValueException {  
	 		// check if token is in session
	 		User user = getUserFromToken(accessToken);

	 		result = user.createCalcSheet(name, rows, columns);
	 	}


	 	public CalcSheet getResult() {
	 		return result;
	 	}

}
