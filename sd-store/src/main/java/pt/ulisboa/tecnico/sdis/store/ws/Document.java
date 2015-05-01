package pt.ulisboa.tecnico.sdis.store.ws;

public class Document {
	private String name;
	private String tag;
	private byte[] content;
	
	public Document(String name) {
		super();
		this.name = name;
	}
	
	
	public Document(String name, byte[] content) {
		super();
		this.name = name;
		this.content = content;
	}


	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
}
