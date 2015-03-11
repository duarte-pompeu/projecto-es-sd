package pt.tecnico.bubbledocs;


import java.util.ArrayList;

import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;
import pt.tecnico.bubbledocs.dml.BubbleDocs;
import pt.tecnico.bubbledocs.dml.CalcSheet;
import pt.tecnico.bubbledocs.dml.User;


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
    		getAllPeople();
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
	 	CalcSheet cal = user1.createCalcSheet("Notas ES",300,20, false); //not working fine
	 	/*Cell c = new Cell (3,4, new Literal(5));
	 	Cell c2 = new Cell (4,3, new Reference(c));
	 	c2.setContent(new Reference(c));
	 	cal.getCellSet().add(c);
	 	cal.getCellSet().add(c2);*/
	 	pb.getCalcSheetSet().add(cal); //not working fine
	 	
	/*
	 	org.jdom2.Document jdomDoc = new org.jdom2.Document();
	 	jdomDoc.setRootElement(pb.getCalcSheetSet().iterator().next().exportToXML());
	 	XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		System.out.println(xml.outputString(jdomDoc));
	*/

	 	 
}

	
	
	
	 @Atomic
	    static void getAllPeople() {
		 BubbleDocs pb = BubbleDocs.getInstance();
		 	for (User p : pb.getUserSet()) {
		 		System.out.println(p.getUserName() +" " + p.getName() + " " + p.getPassword() );
		 	}
	    }
	 
	 @Atomic
	 static Iterable<CalcSheet> getAllSheets(User u){
		 ArrayList<CalcSheet> list = new ArrayList<CalcSheet>();
		 BubbleDocs bd = BubbleDocs.getInstance();
		 for(CalcSheet cs: bd.getCalcSheetSet()){
			 if(cs.getCreator().getUserName() == u.getUserName()){
				 list.add(cs);
			 }
		 }
		 return list;
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



