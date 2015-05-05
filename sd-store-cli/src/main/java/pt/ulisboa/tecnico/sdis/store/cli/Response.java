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
		return this.TYPE == r.TYPE && this.docContent.equals(r.docContent);
	}


	public byte[] getContent() {
		return docContent;
	}
}
