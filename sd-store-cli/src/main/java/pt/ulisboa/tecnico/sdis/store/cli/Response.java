package pt.ulisboa.tecnico.sdis.store.cli;

public class Response {
	private static final int CONTENT = 123;
	private static final int EXCEPTION = 124;
	
	public final int TYPE;
	public byte[] docContent = null;
	
	
	public Response(byte[] content){
		TYPE = CONTENT;
		docContent = content;
	}
	

	public boolean equals(Response r){
		return this.TYPE == r.TYPE && compareByteArrays(this.docContent, r.docContent);
	}
	
	public boolean compareByteArrays(byte[] ba1, byte[] ba2){
		if(ba1.length != ba2.length){
			return false;
		}
		
		for(int i = 0; i < ba1.length; i++){
			if(ba1[i] != ba2[i]){
				return false;
			}
		}
		
		return true;
	}


	public byte[] getContent() {
		return docContent;
	}
}
