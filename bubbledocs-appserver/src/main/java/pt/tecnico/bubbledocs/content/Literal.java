package pt.tecnico.bubbledocs.content;

public class Literal extends SimpleContent {

	private int value;
	
	public Literal(int value){
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
}
