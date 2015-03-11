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
    		printDomainInXML(convertToXML());
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
		
		// setup the initial state if BubbleDocs is empty

		User user1 = new User("pf","Paul Door","sub");
	 	pb.addUser(user1);
	 	User user2 = new User("ra","Step Rabbit","cor");
	 	pb.addUser(user2);
	 	CalcSheet a=new CalcSheet("like any other", 10, 10);
	 	pb.addCalcSheet(a);
}

	
	
	
	 @Atomic
	    static void getAllPeople() {
		 BubbleDocs pb = BubbleDocs.getInstance();
		 	for (User p : pb.getUserSet()) {
		 		System.out.println(p.getUserName() +" " + p.getName() + " " + p.getPassword() );
		 	}
	    }
	 	
	 // FIXME: cant test due to issue #4 - calcsheets arent added to fenix or BD
	 // TODO: test when that's corrected
	@Atomic
	static Iterable<CalcSheet> getAllSheets(User u, String substring){
		ArrayList<CalcSheet> csList = new ArrayList<CalcSheet>();
		for(CalcSheet cs: u.getCreatedCalcSheetSet()){
			if(cs.getName().contains(substring)){
				csList.add(cs);
			}
		}
		
		return csList;
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



