package pt.ulisboa.tecnico.sdis.store.cli;

public class QuorumFactory {
	int _nVoters;
	int _RT;
	int _WT;
	
	
	public QuorumFactory(int numberOfVoters){
		_nVoters = numberOfVoters;
	}
	
	
	public Quorum getNewQuorum(){
		return new Quorum(_nVoters);
	}
	
	
	public Quorum getNewWriteQuorum(){
		return new Quorum(_nVoters, _WT);
	}
	
	
	public Quorum getNewReadQuorum(){
		return new Quorum(_nVoters, _RT);
	}
}
