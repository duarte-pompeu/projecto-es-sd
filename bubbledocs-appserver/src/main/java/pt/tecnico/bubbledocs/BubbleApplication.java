package pt.tecnico.bubbledocs;


import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;
import pt.tecnico.bubbledocs.dml.Add;
import pt.tecnico.bubbledocs.dml.BubbleDocs;
import pt.tecnico.bubbledocs.dml.CalcSheet;
import pt.tecnico.bubbledocs.dml.Cell;
import pt.tecnico.bubbledocs.dml.User;
import pt.tecnico.bubbledocs.dml.Literal;
import pt.tecnico.bubbledocs.dml.Reference;


public class BubbleApplication {
	public static void main(String args[]){
		System.out.println("Welcome to the BubbleDocs application!");
		
		TransactionManager tm = FenixFramework.getTransactionManager();
    	boolean committed = false;
    	BubbleDocs pb=null;
    	try {
    		tm.begin();
    		pb = BubbleDocs.getInstance();
    		populateDomain(pb);
    		tm.commit();
    		committed = true;
    	}catch (Exception ex) {
    	    System.err.println("Error in execution of transaction: " + ex);
    	} finally {
    	    if (!committed) 
    		try {
    		    tm.rollback();
    		} catch (Exception ex) {
    		    System.err.println("Error in roll back of transaction: " + ex);
    		}
    	    
    	    
    	}
   
    	
	}
	
	static void populateDomain(BubbleDocs pb) {
		if (!pb.getUserSet().isEmpty())
		   return;

		// setup the initial state if BubbleDocs is empty

		User user1 = new User("pf","Paul Door","sub");
	 	pb.getUserSet().add(user1);
	 	User user2 = new User("ra","Step Rabbit","cor");
	 	CalcSheet cal = user1.createCalcSheet("Notas ES",300,20);
	 	Cell c = new Cell (3,4);
	 	c.setContent(new Literal(5));
	 	cal.getCellSet().add(c);
	 	pb.getCalcSheetSet().add(cal);
}
	
}