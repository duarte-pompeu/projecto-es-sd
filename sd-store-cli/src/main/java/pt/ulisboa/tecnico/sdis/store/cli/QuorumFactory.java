package pt.ulisboa.tecnico.sdis.store.cli;

public class QuorumFactory {
	int _nVoters;
	int _RT;
	int _WT;
	
	
	public QuorumFactory(int numberOfVoters){
		this(numberOfVoters, -1, -1);
	}
	
	
	public QuorumFactory(int numberOfVoters, int readThreshold, int writeThreshold){
		_nVoters = numberOfVoters;
		_RT = readThreshold;
		_WT = writeThreshold;
	}
	
	
	public Quorum getNewQuorum(){
		return new Quorum(_nVoters);
	}
	
	
	public Quorum getNewWriteQuorum(){
		if(_WT == -1){
			return new Quorum(_nVoters);
		}
		
		return new Quorum(_nVoters, _WT);
	}
	
	
	public Quorum getNewReadQuorum(){
		if(_RT == -1){
			return new Quorum(_nVoters);
		}
		return new Quorum(_nVoters, _RT);
	}
}
