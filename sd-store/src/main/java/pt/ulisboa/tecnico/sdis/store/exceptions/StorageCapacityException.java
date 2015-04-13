package pt.ulisboa.tecnico.sdis.store.exceptions;

public class StorageCapacityException extends Exception {

	private static final long serialVersionUID = 8586594829954149270L;
	
	
	public StorageCapacityException(){
		super();
	}
	
	public StorageCapacityException(String message){
		super(message);
	}
}
