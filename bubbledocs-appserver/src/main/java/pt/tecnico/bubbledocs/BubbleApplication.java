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
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.service.AssignLiteralCell;
import pt.tecnico.bubbledocs.service.AssignReferenceCell;
import pt.tecnico.bubbledocs.service.CreateSpreadSheet;
import pt.tecnico.bubbledocs.service.CreateUser;
import pt.tecnico.bubbledocs.service.LoginUser;


/**
 * @author Diogo, Duarte, Tiago
 *	This is the class which contains the main method.
 *	It also contains several static methods that are used in main.
 */
public class BubbleApplication {
	private static final String DOC = "Notas ES";
	/**
	 * @param args Not used at the moment.
	 */
	static TransactionManager tm;
	public static void main(String args[]) {
		System.out.println("Welcome to the BubbleDocs application!");

		tm = FenixFramework.getTransactionManager();
		boolean committed = false;

		try {
		
			tm.begin();
			populateDomain();
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
	 * @throws SystemException 
	 * @throws HeuristicRollbackException 
	 * @throws HeuristicMixedException 
	 * @throws RollbackException 
	 * @throws IllegalStateException 
	 * @throws SecurityException 
	 * @throws NotSupportedException 
	 */
	static void populateDomain() throws SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException, SystemException, NotSupportedException {
    		
		BubbleDocs pb=BubbleDocs.getInstance();
		if (!pb.getUserSet().isEmpty()){
			System.out.println(pb.getCalcSheetByName(DOC).markdownPrint());
			return;
		}
		    
		
		String root_token, pf_token;
		LoginUser login;
		int sheet_id;

		//criar o root e colocá-lo em sessão
		BubbleDocs.getInstance().addUser("root", "root@rootmail.root", "Super User", "rootroot");
		login = new LoginUser("root", "rootroot");
		login.execute();
		root_token = login.getUserToken();
		
		CreateUser createService = new CreateUser(root_token, "pf_abc", "sub", "Paul Door");
		createService.execute();
		
		new CreateUser(root_token, "ra_abc", "cor", "Step Rabbit").execute();		

		login = new LoginUser("pf_abc", "sub");
		login.execute();
		pf_token = login.getUserToken();
		
		CreateSpreadSheet spread = new CreateSpreadSheet(pf_token, DOC, 300, 20);
		spread.execute();
		sheet_id = spread.getResult().getId();
		
		
		new AssignLiteralCell(pf_token, sheet_id, "3;4", "5").execute();
		new AssignReferenceCell(pf_token, sheet_id, "1;1", "5;6").execute();
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



