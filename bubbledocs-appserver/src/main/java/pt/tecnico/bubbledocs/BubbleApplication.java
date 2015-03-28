package pt.tecnico.bubbledocs;


import java.util.ArrayList;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;
import pt.tecnico.bubbledocs.dml.Add;
import pt.tecnico.bubbledocs.dml.BubbleDocs;
import pt.tecnico.bubbledocs.dml.CalcSheet;
import pt.tecnico.bubbledocs.dml.Div;
import pt.tecnico.bubbledocs.dml.Literal;
import pt.tecnico.bubbledocs.dml.LiteralArgument;
import pt.tecnico.bubbledocs.dml.Reference;
import pt.tecnico.bubbledocs.dml.ReferenceArgument;
import pt.tecnico.bubbledocs.dml.User;


/**
 * @author Diogo, Duarte, Tiago
 *	This is the class which contains the main method.
 *	It also contains several static methods that are used in main.
 */
public class BubbleApplication {
	/**
	 * @param args Not used at the moment.
	 */
	public static void main(String args[]) {
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
			// ponto 3 do enunciado
			User[] users = new User[2];
			users[0] = getUser(BubbleDocs.getInstance(), "pf");
			users[1] = getUser(BubbleDocs.getInstance(), "ra");
			for(User u: users){
				System.out.println("Calcsheets created by " + u.getName() + ":");
				for(CalcSheet cs: u.getCreatedCalcSheetSet()){
					System.out.println(cs.getName());
				}
				System.out.println("END");
			}
			tm.commit();

			tm.begin();
			// ponto 4 do enunciado
			ArrayList<org.jdom2.Document> doc=new ArrayList<org.jdom2.Document>();
			doc=printAllCalcSheetsFromUser("pf",doc);
			tm.commit();

			tm.begin();
			// ponto 5 do enunciado
			removeCalcSheet("Notas Es","pf"); 
			tm.commit();

			tm.begin();
			// ponto 6 do enunciado
			getThisUsersCalcSheets("pf");
			tm.commit();

			tm.begin();
			// ponto 7 do enunciado
			recoverFromBackup(doc.get(0));
			tm.commit();

			tm.begin();
			// ponto 8 do enunciado
			getThisUsersCalcSheets("pf");
			tm.commit();

			tm.begin();
			// ponto 9 do enunciado
			doc=printAllCalcSheetsFromUser("pf",doc);
			tm.commit();

			committed = true;

		}catch (SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException ex) {
			ex.printStackTrace();
		} finally {
			if (!committed){ 
				try {
					tm.rollback();
				} catch (SystemException ex) {
					System.err.println("Error in roll back of transaction: " + ex);
				}
			}
		}
	}
	
	
	/**
	 * This function prints to stdout information about the calcSheets of a specific user.
	 * @param userName The user's user name.
	 */
	private static void getThisUsersCalcSheets(String userName){
		
		User u = getUser(BubbleDocs.getInstance(), userName);
		System.out.println("Calcsheets created by " + u.getName() + ":");
		for(CalcSheet cs: u.getCreatedCalcSheetSet()){
			String s = "CalcSheet: name,ID=(" + cs.getName() + ","
					+ cs.getId() + ")";
			System.out.println(s);
		}
		System.out.println("END");	
		
		
		
	}
	
	
	
	
	/**
	 * Given the name of the calcSheet and the user name of its owner, this method will remove the calcSheet
	 * from the persistent state of the application
	 * @param name the calcSheet's creator's name
	 * @param user the name of the calcSheet which will be removed
	 */
	private static void removeCalcSheet(String name, String user) {
	    		
	    		BubbleDocs pb = BubbleDocs.getInstance();
	    		pb.removeCalcSheet(name, user);
	    		
	    }
	
	/**
	 * This method will convert all the calcSheet from a user into XML.
	 * It then prints the resulting XML to the stdout.
	 * It also returns an ArrayList of the user's documents.
	 * @param user the user name of the person whose calcSheets will be converted
	 * @param doc the arrayList in which the converted calcSheets will be saved
	 * @return the arrayList which contains all of the user's calcSheets in XML document format
	 */
	private static ArrayList<org.jdom2.Document> printAllCalcSheetsFromUser(String user, ArrayList<org.jdom2.Document> doc){

		org.jdom2.Document d=null;
		BubbleDocs pb = BubbleDocs.getInstance();
		for(CalcSheet c: pb.getCalcSheetSet()){
			if(c.getCreator().getUserName().compareTo(user)==0)
				printDomainInXML(d=convertToXML(c));}
		if(d!=null)
			doc.add(d);
		return doc;
	}
	
	/**
	 * This method will import a XML document, containing a CalcSheet into the application.
	 * @param jdomDoc The XML document which will be imported.
	 */
	private static void recoverFromBackup(org.jdom2.Document jdomDoc) {
		CalcSheet a=new CalcSheet();
		
		a.importFromXML(jdomDoc.getRootElement());
		
		BubbleDocs.getInstance().getCalcSheetSet().add(a);
	    }
	
	
	/**
	 * This method will verify if the database is empty by verifying if it had any users.
	 * In case the database is empty, it will then populate it.
	 * If it isn't empty then it simply returns. 
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
	 	c1.getCell(5,6).setContent(new Add ( new LiteralArgument (2), new ReferenceArgument(c1.getCell(3,4)) ));
	 	c1.getCell(2,2).setContent(new Div ( new ReferenceArgument(c1.getCell(1,1)), new ReferenceArgument(c1.getCell(3,4)) ));
	 	pb.addCalcSheet(c1);
	 	
}

	
	
	
	
	    /**
	     * This method will print information about each of the users stored in the database.
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
	public static User getUser(BubbleDocs bd, String username) {
			
			return bd.getUser(username);
		}
	
		
	  	/**
	  	 * This method converts a calcSheet into a XML document.
	  	 * @param c The calcSheet which will be converted
	  	 * @return The resulting XML document.
	  	 */
	    public static org.jdom2.Document convertToXML(CalcSheet c) {
		 
	  		

	  		org.jdom2.Document jdomDoc = new org.jdom2.Document();
	
	  		jdomDoc.setRootElement(c.exportToXML());

		return jdomDoc;
	    }

	    /**
	     * This method take a XML jdomDoc object, convert it into a String and print the result to stdout.
	     * @param jdomDoc The document which will be converted and printed to stdout.
	     */
	    public static void printDomainInXML(org.jdom2.Document jdomDoc) {
		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		System.out.println(xml.outputString(jdomDoc));
	    }

}



