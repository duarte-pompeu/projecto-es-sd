package pt.ulisboa.tecnico.sdis.store.cli;

import java.util.ArrayList;

import javax.naming.directory.InvalidAttributeValueException;

import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class Quorum {
	private final int _nVoters;
	private final int _min4quorum;
	private ArrayList<Response> _responses = new ArrayList<Response>();
	private ArrayList<Response> _uniqueResponses = new ArrayList<Response>();
	private ArrayList<Integer> votes = new ArrayList<Integer>();
	
	
	public Quorum(int nVoters){
		_nVoters = nVoters;
		
		if(nVoters % 2 == 0){
			_min4quorum = nVoters / 2 +1;
		}
		else{
			_min4quorum = new Double(Math.ceil(nVoters/2.0)).intValue();
		}
	}
	
	public void addResponse(Response r){
		
		_responses.add(r);
		
		if(isUnique(r)){
			_uniqueResponses.add(r);
			votes.add(1);
		}
		
		else{
			int i = getUniquePos(r);
			
			if(i == -1){
				//error
				return; 
			}
			
			int v = votes.get(i);
			v++;
			votes.set(i, v);
		}
	}

	
	public byte[] getVerdict() {
		
		for(int i = 0; i < votes.size(); i++){
			if(votes.get(i) >= _min4quorum){
				return _uniqueResponses.get(i).getContent();
			}
		}
		
		return null;
		
	}
	
	public boolean isUnique(Response r0){
		for(Response r1: _uniqueResponses){
			
			if(r0.equals(r1)){
				return false;
			}
		}
		
		return true;
	}
	
	public int getUniquePos(Response r0){
		for(int i = 0; i < _uniqueResponses.size(); i++){
			
			Response r1 = _uniqueResponses.get(i);
			
			if(r0.equals(r1)){
				return i;
			}
		}
		
		return -1;
	}


	public int countVotes() {
		return _responses.size();
	}
	
	public int min4quorum(){
		return _min4quorum;
	}
	
	public int countResponses(){
		return _responses.size();
	}
	
	public int countUniqueResponses(){
		return _uniqueResponses.size();
	}
	
	public void addResponse(byte[] content){
		Response r = new Response(content);
		addResponse(r);
	}

	public void addException(InvalidAttributeValueException e) {
		Response r = new Response(e);
		addResponse(r);
	}

	public void addException(DocDoesNotExist_Exception e) {
		Response r = new Response(e);
		addResponse(r);
		
	}

	public void addException(UserDoesNotExist_Exception e) {
		Response r = new Response(e);
		addResponse(r);
	}
}
