package pt.tecnico.bubbledocs;


import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.CalcSheet;
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
			return;
		}
		    
		
		String root_token, pf_token;
		LoginUser login;
		int sheet_id;

		//criar o root e colocá-lo em sessão
		BubbleDocs.getInstance().addUser("root", "root@rootmail.root", "Super User", "rootroot");
		login = new LoginUser("root", "rootroot");
		login.execute();
		root_token = login.getResult();
		
		CreateUser createService = new CreateUser(root_token, "pf_abc", "sub", "Paul Door");
		createService.execute();
		
		new CreateUser(root_token, "ra_abc", "cor", "Step Rabbit").execute();		

		login = new LoginUser("pf_abc", "sub");
		login.execute();
		pf_token = login.getResult();
		
		CreateSpreadSheet spread = new CreateSpreadSheet(pf_token, DOC, 300, 20);
		spread.execute();
		sheet_id = spread.getResult().getId();
		
		
		new AssignLiteralCell(pf_token, sheet_id, "3;4", "5").execute();
		new AssignReferenceCell(pf_token, sheet_id, "1;1", "5;6").execute();
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

}



