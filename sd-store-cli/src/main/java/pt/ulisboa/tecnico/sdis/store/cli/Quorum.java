package pt.ulisboa.tecnico.sdis.store.cli;

import java.util.ArrayList;
import java.util.Collection;

import javax.naming.directory.InvalidAttributeValueException;

import pt.ulisboa.tecnico.sdis.store.exceptions.NoConsensusException;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class Quorum {
	private final int _nVoters;
	private final int _min4quorum;
	private ArrayList<Response> _responses = new ArrayList<Response>();
	private ArrayList<Response> _uniqueResponses = new ArrayList<Response>();
	private ArrayList<Integer> votes = new ArrayList<Integer>();
	
	private boolean totalConsensus = false;
	
	
	public Quorum(int nVoters){
		_nVoters = nVoters;
		
		if(nVoters % 2 == 0){
			_min4quorum = nVoters / 2 +1;
		}
		else{
			_min4quorum = new Double(Math.ceil(nVoters/2.0)).intValue();
		}
	}
	
	public Quorum(int nVoters, int threshold){
		_nVoters = nVoters;
		_min4quorum = threshold;
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

	private Response getVerdict() throws NoConsensusException{
		int highest_votes = 0;
		
		for(int i = 0; i < votes.size(); i++){
			int n_votes = votes.get(i);
			
			if(n_votes >= _min4quorum){
				
				if(n_votes == _responses.size()){
					totalConsensus = true;
				}
				
				return _uniqueResponses.get(i);
			}
			
			if(n_votes > highest_votes)
				highest_votes = n_votes;
		}
		
		if(getVotesLeft() + highest_votes < min4quorum()){
			throw new NoConsensusException("Not enough votes 4 quorum.");
		}
		
		return null;
	}
	
	
	public byte[] getVerdict4content() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException {
		Response r = getVerdict();
		
		if(r == null){
			return null;
		}
		
		return r.getContent();
	}
	
	
	public Collection<String> getVerdict4Collection() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException {
		Response r = getVerdict();
		
		if(r == null){
			return null;
		}
		
		return r.getDocNames();
	}
	
	
	private Response getSeqVerdict(){
		int min4quor = min4quorum();
		
		if(_responses.size() < min4quor){
			return null;
		}
		
		Tag lastTag = new Tag(-1,-1);
		Response result = null;
		
		for(int i = 0; i < min4quor; i++){
			Response tmpRes = _responses.get(i);
			Tag tmpTag = tmpRes.getTag();
			
			if(tmpTag.compareTo(lastTag) > 0){
				lastTag = tmpTag;
				result = tmpRes;
			}
		}
		
		
		return result;
	}
	
	
	public byte[] getSeqVerdict4content() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception{
		Response r = getSeqVerdict();
		
		if(r == null){
			return null;
		}
		
		return r.getContent();
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
	
	public int getVotesLeft(){
		return _nVoters - _uniqueResponses.size();
	}
	
	public void addResponse(byte[] content, int ID){
		Response r = new Response(content, ID);
		addResponse(r);
	}
	
	public void addResponse(Collection<String> content, int ID){
		Response r = new Response(content, ID);
		addResponse(r);
	}

	public void addException(InvalidAttributeValueException e, int ID) {
		Response r = new Response(e, ID);
		addResponse(r);
	}

	public void addException(DocDoesNotExist_Exception e, int ID) {
		Response r = new Response(e, ID);
		addResponse(r);
		
	}

	public void addException(UserDoesNotExist_Exception e, int ID) {
		Response r = new Response(e, ID);
		addResponse(r);
	}

	public void addSuccess(int ID) {
		Response r = new Response(ID);
		addResponse(r);
		
	}

	public void addException(CapacityExceeded_Exception e, int ID) {
		Response r = new Response(e, ID);
		addResponse(r);
		
	}

	public boolean isTotalConsensus() {
		return totalConsensus;
	}
}
