package pt.ulisboa.tecnico.sdis.store.cli;

public class Tag {
	private int seq, frontendID;

	public Tag(int seq, int frontendID) {
		super();
		this.seq = seq;
		this.frontendID = frontendID;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getFrontendID() {
		return frontendID;
	}

	public void setFrontendID(int frontendID) {
		this.frontendID = frontendID;
	}
	
	@Override
	public String toString(){
		return seq + ";" + frontendID;
	}

	public int compareTo(Tag otherTag) {
		if(otherTag == null){
			return 1;
		}
		
		if(this.seq == otherTag.seq){
			return this.frontendID - otherTag.frontendID;
		}
		
		else return this.seq - otherTag.seq;
	}
}
