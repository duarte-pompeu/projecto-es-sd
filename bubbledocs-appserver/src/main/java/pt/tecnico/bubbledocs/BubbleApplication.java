package pt.tecnico.bubbledocs;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.dml.BubbleDocs;
import pt.tecnico.bubbledocs.dml.User;

public class BubbleApplication {
	public static void main(String args[]){
		System.out.println("Welcome to the BubbleDocs application!");
		BubbleDocs pb = BubbleDocs.getInstance();
		    populateDomain(pb);
		
	}
	
	@Atomic
	static void populateDomain(BubbleDocs pb) {
		//if (!pb.getPersonSet().isEmpty())
		  //  return;

		// setup the initial state if BubbleDocs is empty

		User user1 = new User("pf","Paul Door","sub");
	 	pb.addUser(user1);
	 	User user2 = new User("ra","Step Rabbit","cor");
	 	pb.addUser(user2);
}
}