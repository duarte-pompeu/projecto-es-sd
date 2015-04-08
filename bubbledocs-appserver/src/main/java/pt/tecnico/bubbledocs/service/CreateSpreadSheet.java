package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.User;
// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.InvalidFormatException;
import pt.tecnico.bubbledocs.exceptions.InvalidValueException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;

public class CreateSpreadSheet extends BubbleDocsService {
	 	private int result;
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
    	User user;
    	try{
    		user = getSessionFromToken(accessToken).getUser();
    	}
    	catch(BubbleDocsException e){
    		throw e;
    	}
    	
    	//check if name is null
    	if (name.equals(null))
    		throw new NullPointerException("CalcSheet name can't be null");
    	
    	//check if Row Value is greater then 0
    	 if(rows < 1)
    		 throw new InvalidValueException(rows + " isn't greater then zero.");
    	
    	//check if Column Value is greater then 0
    	 if(columns < 1)
    		 throw new InvalidValueException(columns + " isn't greater then zero.");
    
    	 //creates the calcsheet
    	 CalcSheet sheet = user.createCalcSheet(name, rows, columns);
    	 result = sheet.getId();
    }

    
    //GETTERS AND SETTERS
	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
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
