package pt.ulisboa.tecnico.sdis.store.cli.service;

import java.util.List;

import javax.naming.directory.InvalidAttributeValueException;

import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class ListDocsService {
	String userID;
	SDStore port;
	List<String> result;
	
	public ListDocsService(String userID, SDStore port){
		this.userID = userID;
		this.port = port;
	}
	
	public void dispatch() throws UserDoesNotExist_Exception, InvalidAttributeValueException{		
		
		if(port == null ){
			throw new InvalidAttributeValueException("Port is null");
		}
		
		result = port.listDocs(userID);
	}
	
	public List<String> getResult(){
		return result;
	}
}
