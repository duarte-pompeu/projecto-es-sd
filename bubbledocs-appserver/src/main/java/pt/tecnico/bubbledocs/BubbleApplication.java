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


import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


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
	 	pb.getUserSet().add(user2);
	 	//CalcSheet cal = user1.createCalcSheet("Notas ES",300,20, false);
	 	//Cell c = new Cell (3,4, new Literal(5));
	 	//Cell c2 = new Cell (4,3, new Reference(c));
	 	//c2.setContent(new Reference(c));
	 	//cal.getCellSet().add(c);
	 	//cal.getCellSet().add(c2);
	 	//pb.getCalcSheetSet().add(cal);
	 	
	/*
	 	org.jdom2.Document jdomDoc = new org.jdom2.Document();
	 	jdomDoc.setRootElement(pb.getCalcSheetSet().iterator().next().exportToXML());
	 	XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		System.out.println(xml.outputString(jdomDoc));
	*/

	 	
}

	
	
	
	
	
	
	
	
	
	
	//	NONE OF THIS WORKS - HELP. STUFF ONLY WORKS INSIDE A TRANSATION (UNLIKE THE PHONEBOOK???WHATS GOING ON)
	
	
	  	@Atomic
	    public static org.jdom2.Document convertToXML() {
		 
	  		BubbleDocs pb = BubbleDocs.getInstance();

	  		org.jdom2.Document jdomDoc = new org.jdom2.Document();
	
		jdomDoc.setRootElement(pb.getCalcSheetSet().iterator().next().exportToXML());

		return jdomDoc;
	    }

	    @Atomic
	    public static void printDomainInXML(org.jdom2.Document jdomDoc) {
		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		System.out.println(xml.outputString(jdomDoc));
	    }

}



