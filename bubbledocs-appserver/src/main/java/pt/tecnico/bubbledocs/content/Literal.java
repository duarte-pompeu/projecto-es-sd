package pt.tecnico.bubbledocs.content;

public class Literal extends SimpleContent {

	private double value;
	
	public Literal(double value){
		this.value = value;
	}
	
	public double getValue(){
		return this.value;
	}
}
