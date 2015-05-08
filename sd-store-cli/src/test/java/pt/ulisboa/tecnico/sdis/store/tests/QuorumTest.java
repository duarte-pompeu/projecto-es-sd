package pt.ulisboa.tecnico.sdis.store.tests;

import javax.naming.directory.InvalidAttributeValueException;

import org.junit.BeforeClass;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.cli.Quorum;
import pt.ulisboa.tecnico.sdis.store.cli.QuorumFactory;
import pt.ulisboa.tecnico.sdis.store.cli.StoreClient;
import pt.ulisboa.tecnico.sdis.store.exceptions.NoConsensusException;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import static org.junit.Assert.*;

public class QuorumTest extends SDStoreClientTest{
	static final int N_VOTERS = 3;
	static final String CONTENT = "abcdefg";
	static final String ALT_CONTENT1 = "abcdeff";
	static final String ALT_CONTENT2 = "abc";
	
	static Quorum QUORUM;
	static byte[] bcontent;
	static byte[] altbcontent1;
	static byte[] altbcontent2;
	
	static UserDoesNotExist_Exception UDNEex;
	static DocDoesNotExist_Exception DDNEex;
	static InvalidAttributeValueException IAVex;
	
	@BeforeClass
	public static void populate(){
		QUORUM = new Quorum(N_VOTERS);
		
		bcontent = StoreClient.string2bytes(CONTENT);
		altbcontent1 = StoreClient.string2bytes(ALT_CONTENT1);
		altbcontent2 = StoreClient.string2bytes(ALT_CONTENT2);
		
		QUORUM.addResponse(bcontent);
		QUORUM.addResponse(bcontent);
		QUORUM.addResponse(bcontent);
		
		String message = "ERROR!";
		UserDoesNotExist uddne = new UserDoesNotExist();
		uddne.setUserId("Manuel");
		UDNEex = new UserDoesNotExist_Exception(message, uddne);
		
		DocDoesNotExist ddne = new DocDoesNotExist();
		ddne.setDocId("123");
		DDNEex = new DocDoesNotExist_Exception(message, ddne);
		
		IAVex = new InvalidAttributeValueException("no explanation");
		
	}
	
	
	@Test
	public void populateSuccess() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		assertEquals(new Integer(N_VOTERS), new Integer(QUORUM.countVotes()));
		assertEquals(new Integer(N_VOTERS), new Integer(QUORUM.countResponses()));
		assertEquals(new Integer(1), new Integer(QUORUM.countUniqueResponses()));
		
		assertEquals(bcontent, QUORUM.getVerdict());
	}
	
	
	@Test
	public void quorumFactories(){
		QuorumFactory qfact1 = new QuorumFactory(3);
		QuorumFactory qfact2 = new QuorumFactory(3,1,2);
		
		Integer expectedVoters, resultVoters;
		
		expectedVoters = new Integer(2);
		resultVoters = new Integer(qfact1.getNewQuorum().min4quorum());
		assertEquals(expectedVoters, resultVoters);
		
		expectedVoters = new Integer(1);
		resultVoters = new Integer(qfact2.getNewReadQuorum().min4quorum());
		assertEquals(expectedVoters, resultVoters);
		
		expectedVoters = new Integer(2);
		resultVoters = new Integer(qfact2.getNewWriteQuorum().min4quorum());
		assertEquals(expectedVoters, resultVoters);
	}
	
	
	@Test
	public void min4quorum(){
		Quorum q1 = new Quorum(1);
		Quorum q3 = new Quorum(3);
		Quorum q4 = new Quorum(4);
		Quorum q10k = new Quorum(10000);
		
		
		assertEquals(new Integer(1), new Integer(q1.min4quorum()));
		assertEquals(new Integer(2), new Integer(q3.min4quorum()));
		assertEquals(new Integer(3), new Integer(q4.min4quorum()));
		assertEquals(new Integer(5001), new Integer(q10k.min4quorum()));
	}
	
	
	@Test
	public void singleQuorum() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		Quorum quorum = new Quorum(1);
		quorum.addResponse(bcontent);
		
		assertEquals(new Integer(1), new Integer(quorum.countVotes()));
		assertEquals(new Integer(1), new Integer(quorum.countResponses()));
		assertEquals(new Integer(1), new Integer(quorum.countUniqueResponses()));
		
		assertEquals(bcontent, quorum.getVerdict());
	}
	
	
	@Test
	public void notEnoughVotes() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		Quorum quorum = new Quorum(3);
		
		quorum.addResponse(bcontent);
		
		assertNull(quorum.getVerdict());
	}
	
	
	@Test (expected = NoConsensusException.class)
	public void noConsensus() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		Quorum quorum = new Quorum(3);
		
		quorum.addResponse(string2bytes("HEY"));
		quorum.addResponse(string2bytes("HI"));
		quorum.addResponse(string2bytes("HELLO"));
		
		quorum.getVerdict();
	}
	
	
	@Test
	public void notAllVotesButPass() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		Quorum quorum = new Quorum(3);
		
		quorum.addResponse(bcontent);
		quorum.addResponse(bcontent);
		
		assertEquals(bcontent, quorum.getVerdict());
	}
	
	
	@Test
	public void notUnanimousButPass1() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		Quorum quorum = new Quorum(3);
		
		quorum.addResponse(altbcontent1);
		quorum.addResponse(bcontent);
		quorum.addResponse(bcontent);
		
		assertEquals(bcontent, quorum.getVerdict());
	}
	
	
	@Test
	public void notUnanimousButPass2() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		Quorum quorum = new Quorum(3);
		
		quorum.addResponse(bcontent);
		quorum.addResponse(altbcontent1);
		quorum.addResponse(bcontent);
		
		assertEquals(bcontent, quorum.getVerdict());
	}
	
	
	@Test
	public void notUnanimousButPass3() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		Quorum quorum = new Quorum(3);
		
		quorum.addResponse(bcontent);
		quorum.addResponse(bcontent);
		quorum.addResponse(altbcontent1);
		
		assertEquals(bcontent, quorum.getVerdict());
	}
	
	
	@Test
	public void oneExceptButPass() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		Quorum quorum = new Quorum(3);
		
		quorum.addException(UDNEex);
		quorum.addResponse(bcontent);
		quorum.addResponse(bcontent);
			
		assertEquals(bcontent, quorum.getVerdict());
	}
	
	
	@Test (expected = UserDoesNotExist_Exception.class)
	public void twoExceptionsFail1() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		Quorum quorum = new Quorum(3);
		
		quorum.addException(UDNEex);
		quorum.addException(UDNEex);
		quorum.addResponse(bcontent);
			
		assertEquals(bcontent, quorum.getVerdict());
	}
	
	
	@Test (expected = DocDoesNotExist_Exception.class)
	public void twoExceptionsFail2() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		Quorum quorum = new Quorum(3);
		
		quorum.addException(DDNEex);
		quorum.addException(DDNEex);
		quorum.addResponse(bcontent);
			
		assertEquals(bcontent, quorum.getVerdict());
	}
	
	
	@Test (expected = InvalidAttributeValueException.class)
	public void twoExceptionsFail3() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		Quorum quorum = new Quorum(3);
		
		quorum.addException(IAVex);
		quorum.addException(IAVex);
		quorum.addResponse(bcontent);
			
		assertEquals(bcontent, quorum.getVerdict());
	}
	
	
	@Test
	public void lowThreshold() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		Quorum quorum = new Quorum(3,1);
		assertNull(quorum.getVerdict());
		
		quorum.addResponse(bcontent);
		assertEquals(bcontent, quorum.getVerdict());
	}
	
	
	@Test
	public void HighThreshold() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		Quorum quorum = new Quorum(3,3);
		assertNull(quorum.getVerdict());
		
		quorum.addResponse(bcontent);
		assertNull(quorum.getVerdict());
		
		quorum.addResponse(bcontent);
		assertNull(quorum.getVerdict());
		
		quorum.addResponse(bcontent);
		assertEquals(bcontent, quorum.getVerdict());
	}
}
