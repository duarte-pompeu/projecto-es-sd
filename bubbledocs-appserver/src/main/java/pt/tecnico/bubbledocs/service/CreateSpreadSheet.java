package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.InvalidFormatException;
import pt.tecnico.bubbledocs.exceptions.InvalidValueException;
import pt.tecnico.bubbledocs.exceptions.LoginException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;

public class CreateSpreadSheet extends BubbleDocsService {
	 	private String result;
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
    	BubbleDocs bd = BubbleDocs.getInstance();
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
    	 user.createCalcSheet(name, rows, columns);
    }

    
    //GETTERS AND SETTERS
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
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
