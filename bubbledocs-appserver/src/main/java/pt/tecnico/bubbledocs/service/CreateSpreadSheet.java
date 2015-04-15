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
    public CreateSpreadSheet(String userToken, String name, int rows,int columns) {
	    this.setAccessToken(userToken);
	    this.setName(name);
	    this.setRows(rows);
	    this.setColumns(columns);
    	
    }

    @Override
    protected void dispatch() throws BubbleDocsException,UserNotInSessionException,InvalidFormatException, InvalidValueException {  
    	// check if token is in session
    	User user = getUserFromToken(accessToken);
    
    	result = user.createCalcSheet(name, rows, columns);
    }

    
    //GETTERS AND SETTERS
	public CalcSheet getResult() {
		return result;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

}
