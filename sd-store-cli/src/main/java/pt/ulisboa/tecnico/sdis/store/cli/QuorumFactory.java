package pt.ulisboa.tecnico.sdis.store.cli;

public class QuorumFactory {
	int _nVoters;
	
	public QuorumFactory(int numberOfVoters){
		_nVoters = numberOfVoters;
	}
	
	public Quorum getNewquorum(){
		return new Quorum(_nVoters);
	}
}
