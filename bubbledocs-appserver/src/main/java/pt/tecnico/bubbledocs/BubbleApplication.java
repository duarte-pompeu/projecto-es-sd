package pt.tecnico.bubbledocs;


import java.util.ArrayList;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;
import pt.tecnico.bubbledocs.dml.*;


/**
 * @author pc-w
 *
 */
public class BubbleApplication {
	/**
	 * @param args
	 * @throws HeuristicRollbackException 
	 * @throws HeuristicMixedException 
	 * @throws RollbackException 
	 * @throws IllegalStateException 
	 * @throws SecurityException 
	 */
	public static void main(String args[]) throws SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		System.out.println("Welcome to the BubbleDocs application!");
		
		TransactionManager tm = FenixFramework.getTransactionManager();
    	boolean committed = false;

    	try {
    		tm.begin();
    		//ponto 1 do enunciado
    		populateDomain();
    		tm.commit();
		    
    		tm.begin();
    		//ponto 2 do enunciado
    		getAllPeople();
    		tm.commit();
    		
    		tm.begin();
    		//ponto 4 do enunciado
    		printAllCalcSheetsFromUser("pf");
    		tm.commit();
    		
    		tm.begin();
    		//removeCalcSheet("Notas Es"); //still buggy
    		tm.commit();
    		
		    committed = true;
			
    		}catch (SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException ex) {
		    System.err.println("Error in execution of transaction: " + ex);
		} finally {
		    if (!committed) 
			try {
			    tm.rollback();
			} catch (SystemException ex) {
			    System.err.println("Error in roll back of transaction: " + ex);
			}}
	}
	
	/**
	 * @param name
	 */
	
	 @Atomic
	    static void removeCalcSheet(String name) {
	    		
	    		BubbleDocs pb = BubbleDocs.getInstance();
	    		for(CalcSheet c: pb.getCalcSheetSet()){
	    			if(c.getName().compareTo(name)==0)
	    				pb.removeCalcSheet(c);
	    		}
	    }
	
	
	/**
	 * @param user
	 */
	private static void printAllCalcSheetsFromUser(String user){
		
	
		BubbleDocs pb = BubbleDocs.getInstance();
		for(CalcSheet c: pb.getCalcSheetSet()){
			if(c.getCreator().getUserName().compareTo(user)==0)
				printDomainInXML(convertToXML(c));}
		
	}
	
	/**
	 * @param jdomDoc
	 */
	private static void recoverFromBackup(org.jdom2.Document jdomDoc) {
		CalcSheet a=new CalcSheet();
		
		a.importFromXML(jdomDoc.getRootElement());
		
		BubbleDocs.getInstance().getCalcSheetSet().add(a);
	    }
	
	
	/**
	 * 
	 */
	static void populateDomain() {
    		
		BubbleDocs pb=BubbleDocs.getInstance();
		if (!pb.getUserSet().isEmpty() )
		    return;
		
		// setup the initial state if BubbleDocs is empty

		User user1 = new User("pf","Paul Door","sub");
	 	pb.addUser(user1);
	 	User user2 = new User("ra","Step Rabbit","cor");
	 	pb.addUser(user2);

	 	CalcSheet c1 = user1.createCalcSheet("Notas Es", 300, 20);

	 	c1.getCell(3, 4).setContent(new Literal(5));
	 	c1.getCell(1,1).setContent(new Reference(c1.getCell(5,6)));
	 	c1.getCell(5,6).setContent(new Add ( new Literal (2), new Reference(c1.getCell(3,4)) ));
	 	c1.getCell(2,2).setContent(new Div ( new Reference(c1.getCell(1,1)), new Reference(c1.getCell(3,4)) ));
	 	pb.addCalcSheet(c1);
	 	
}

	
	
	
	
	    /**
	     * 
	     */
	    static void getAllPeople(){
	   
		 BubbleDocs pb = BubbleDocs.getInstance();
		 	for (User p : pb.getUserSet()) {
		 		System.out.println(p.getUserName() +" " + p.getName() + " " + p.getPassword() );
		 	}
		 	
	    }
	 	
	 
	/**
	 * @param u
	 * @param substring
	 * @return
	 */

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
	
	
	  	/**
	  	 * @param c
	  	 * @return
	  	 */
	  	
	    public static org.jdom2.Document convertToXML(CalcSheet c) {
		 
	  		

	  		org.jdom2.Document jdomDoc = new org.jdom2.Document();
	
	  		jdomDoc.setRootElement(c.exportToXML());

		return jdomDoc;
	    }

	    /**
	     * @param jdomDoc
	     */
	    
	    public static void printDomainInXML(org.jdom2.Document jdomDoc) {
		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		System.out.println(xml.outputString(jdomDoc));
	    }

}



