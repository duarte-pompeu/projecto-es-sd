package pt.ulisboa.tecnico.sdis.store.ws;

public class Document {
	private String name;
	private byte[] content;
	private int version;
	private String lastUserWrite;
	
	public Document(String name) {
		this(name, null);
	}
	
	
	public Document(String name, byte[] content) {
		super();
		this.name = name;
		this.content = content;
		this.version = 0;
	}


	public int getVersion() {
		return version;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		version += 1;
		this.content = content;
	}

	public void setLastUserWrite(String clientID) {
		this.lastUserWrite = clientID;	
	}
	
	public String getLastUserWrite(){
		return this.lastUserWrite;
	}
}
