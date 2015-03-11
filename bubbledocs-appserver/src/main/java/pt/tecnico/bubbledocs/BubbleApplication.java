package pt.tecnico.bubbledocs;


import java.util.ArrayList;

import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;

import pt.tecnico.bubbledocs.dml.*;


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
    		
    		org.jdom2.Document doc;
    		printDomainInXML(doc=convertToXML());
    		
    		pb.getCalcSheetSet().clear();
    		
    		recoverFromBackup(doc);
    		
    		System.out.println(pb.getCalcSheetSet().size());
    		
    		printDomainInXML(doc=convertToXML());
    		
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
	
	
	private static void recoverFromBackup(org.jdom2.Document jdomDoc) {
		CalcSheet a=new CalcSheet();
		
		a.importFromXML(jdomDoc.getRootElement());
		
		BubbleDocs.getInstance().getCalcSheetSet().add(a);
	    }
	
	
	static void populateDomain(BubbleDocs pb) {
		
		// setup the initial state if BubbleDocs is empty

		User user1 = new User("pf","Paul Door","sub");
	 	pb.addUser(user1);
	 	User user2 = new User("ra","Step Rabbit","cor");
	 	pb.addUser(user2);

	 	CalcSheet c1 = user1.createCalcSheet("Notas Es", 300, 20, false);
	 	//c1.setContent(user1, new Literal(5), 3, 4); getting permission exception, but this user has permission to write 
	
	 	//while permission bug not fixed, using old methods
	 	c1.getCell(3, 4).setContent(new Literal(5));
	 	c1.getCell(1,1).setContent(new Reference(c1.getCell(5,6)));
	 	c1.getCell(5,6).setContent(new Add ( new Literal (2), new Reference(c1.getCell(3,4)) ));
	 	//c1.getCell(2,2).setContent(new Div ( new Reference(c1.getCell(1,1)), new Reference(c1.getCell(3,4)) ));
	 	pb.addCalcSheet(c1);
	 
	 	
	 //	a.getCell(1,1).setContent(new Div(new Literal(2), new Reference(a.getCell(1,2))));
	 	

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



