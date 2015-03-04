package pt.tecnico.bubbledocs.content;

public class Literal extends SimpleContent {

	private int value;
	
	public Literal(int value){
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
	
	@Override
	public String toString(){
		return Integer.toString(value);
	}
}
