package pt.tecnico.bubbledocs.integration.system;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import mockit.Mocked;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class LocalSystemTest {
	
		@Mocked
		IDRemoteServices remote;
		BubbleDocs bd;
		static TransactionManager tm;
		
	    @Before
	    public void setUp() throws Exception {
	    	bd = BubbleDocs.getInstance();
	    }

	    @After
	    public void tearDown() {
	    	bd.deleteBubbleDocs();
	    }
	    
	    @Test
		public void doSequence() throws Exception {
	    	
	    	
	    	tm = FenixFramework.getTransactionManager();
			boolean committed = false;

			try {
			
				tm.begin();
				//integrators go here
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
}

