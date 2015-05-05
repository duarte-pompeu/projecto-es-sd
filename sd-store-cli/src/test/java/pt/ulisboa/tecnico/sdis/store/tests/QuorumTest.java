package pt.ulisboa.tecnico.sdis.store.tests;

import org.junit.BeforeClass;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.cli.Quorum;
import pt.ulisboa.tecnico.sdis.store.cli.StoreClient;
import static org.junit.Assert.*;

public class QuorumTest {
	static final int N_VOTERS = 3;
	static final String CONTENT = "abcdefg";
	
	static Quorum QUORUM;
	static byte[] bcontent;
	
	@BeforeClass
	public static void populate(){
		QUORUM = new Quorum(N_VOTERS);
		bcontent = StoreClient.string2bytes(CONTENT);
		
		QUORUM.addResponse(bcontent);
		QUORUM.addResponse(bcontent);
		QUORUM.addResponse(bcontent);
	}
	
	@Test
	public void populateSuccess(){
		assertEquals(new Integer(N_VOTERS), new Integer(QUORUM.countVotes()));
		assertEquals(new Integer(N_VOTERS), new Integer(QUORUM.countResponses()));
		assertEquals(new Integer(1), new Integer(QUORUM.countUniqueResponses()));
		
		assertEquals(bcontent, QUORUM.getVerdict());
	}
	
	@Test
	public void min4quorum(){
		Quorum q1 = new Quorum(1);
		Quorum q3 = new Quorum(3);
		Quorum q4 = new Quorum(4);
		
		q1.addResponse(bcontent);
		q1.addResponse(bcontent);
		
		assertEquals(new Integer(1), new Integer(q1.min4quorum()));
		assertEquals(new Integer(2), new Integer(q3.min4quorum()));
		assertEquals(new Integer(3), new Integer(q4.min4quorum()));
	}
	
	@Test
	public void singleQuorum(){
		Quorum quorum = new Quorum(1);
		quorum.addResponse(bcontent);
		
		assertEquals(new Integer(1), new Integer(quorum.countVotes()));
		assertEquals(new Integer(1), new Integer(quorum.countResponses()));
		assertEquals(new Integer(1), new Integer(quorum.countUniqueResponses()));
		
		assertEquals(bcontent, quorum.getVerdict());
	}
	
	@Test
	public void notEnoughVotes(){
		Quorum quorum = new Quorum(3);
		
		quorum.addResponse(bcontent);
		
		assertNull(quorum.getVerdict());
	}
	
	@Test
	public void not100butEnough(){
		Quorum quorum = new Quorum(3);
		
		quorum.addResponse(bcontent);
		quorum.addResponse(bcontent);
		
		assertEquals(bcontent, quorum.getVerdict());
	}
}
