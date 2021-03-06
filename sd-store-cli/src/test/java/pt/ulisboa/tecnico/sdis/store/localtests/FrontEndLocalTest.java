package pt.ulisboa.tecnico.sdis.store.localtests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import javax.naming.directory.InvalidAttributeValueException;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.cli.ClientFrontEnd;
import pt.ulisboa.tecnico.sdis.store.cli.StoreClient;
import pt.ulisboa.tecnico.sdis.store.cli.Tag;
import pt.ulisboa.tecnico.sdis.store.exceptions.NoConsensusException;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

/**
 * Esta classe simula o comportamento de várias componentes do FrontEnd, com excepção de StoreClient, que é mocked.
 * Justificação:
 *  - houve problemas em inicializar vários StoreClients com mocked ports.
 *  - a classe StoreClient foi testada intensivamente na 1ª parte do projecto.
 * 
 * 
 * Quando o protocolo Quorum Consensus foi corrigido, alguns testes também tiveram que ser corrigidos ou até eliminados,
 * pois tinham deixado de fazer sentido.
 * 
 */
public class FrontEndLocalTest extends SDStoreClientTest {
	static ArrayList<StoreClient> _clients;
	static ClientFrontEnd _fe;
	
	static String USER = "Manuel";
	static String DOC = "documento";
	static String DOC2 = "document";
	static byte[] CONTENT = string2bytes("TEST");
	static Collection<String> lol;
	
	@Mocked
	StoreClient mockCli1, mockCli2, mockCli3;
	
	@Before
	public void beforeTest(){
		_clients = new ArrayList<StoreClient>();
		lol= new ArrayList<String>();
		lol.add(DOC);
		_clients.add(mockCli1);
		_clients.add(mockCli2);
		_clients.add(mockCli3);
		
		_fe = new ClientFrontEnd(_clients);
	}
	
	
	@Test
	public void store() 
			throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		
		new Expectations() {{
			mockCli1.storeDoc(USER, DOC, CONTENT);
			mockCli2.storeDoc(USER, DOC, CONTENT);
			mockCli3.storeDoc(USER, DOC, CONTENT);
		}};
		
		_fe.storeDoc(USER, DOC, CONTENT);
	}
	
	
	@Test
	public void load() 
			throws InvalidAttributeValueException, DocDoesNotExist_Exception, UserDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		
		new Expectations() {{
			mockCli1.loadDoc(USER, DOC);
			mockCli2.loadDoc(USER, DOC);
			mockCli3.loadDoc(USER, DOC);
			
			result = CONTENT;
		}};
		
		assertEquals(bytes2string(CONTENT), bytes2string(_fe.loadDoc(USER, DOC)));
	}
	
	
	@Test (expected = UserDoesNotExist_Exception.class)
	public void storeException() 
			throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		
		String message = "ERROR!";
		UserDoesNotExist uddne = new UserDoesNotExist();
		uddne.setUserId("Manuel");
		final UserDoesNotExist_Exception UDNEex = new UserDoesNotExist_Exception(message, uddne);
		
		new Expectations() {{
			mockCli1.storeDoc(USER, DOC, CONTENT);
			result = CONTENT;
		}};
		
		new Expectations() {{
			mockCli2.storeDoc(USER, DOC, CONTENT);
			result = UDNEex;
		}};
		
		new Expectations() {{
			mockCli3.storeDoc(USER, DOC, CONTENT);
			result = UDNEex;
		}};
		
		_fe.storeDoc(USER, DOC, CONTENT);
	}
	
	
	@Test
	public void listDocument() 
			throws InvalidAttributeValueException, DocDoesNotExist_Exception, UserDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		
		new Expectations() {{
			mockCli1.listDocs(USER);
			result = lol;
		}};
		
		new Expectations() {{
			mockCli2.listDocs(USER);
			result = lol;
		}};	
		
		new Expectations() {{
			mockCli3.listDocs(USER);
			result = lol;
		}};
		
		assertArrayEquals(lol.toArray(), (_fe.listDocs(USER).toArray()));	
	}
	
	
	@Test
	public void createDocument() 
			throws InvalidAttributeValueException, DocDoesNotExist_Exception, UserDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException, DocAlreadyExists_Exception{
		
		new Expectations() {{
			mockCli1.createDoc(USER, DOC2);
		}};
		
		new Expectations() {{
			mockCli2.createDoc(USER, DOC2);
		}};	
		
		new Expectations() {{
			mockCli3.createDoc(USER, DOC2);
		}};
		
		_fe.createDoc(USER, DOC2);
		
	}
	
	
	/**
	 * Teste para confirmar se resultado obtido equivale a resposta com TAG mais recente.
	 */
	@Test
	public void testTag1() throws InvalidAttributeValueException, DocAlreadyExists_Exception, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		final byte [] expectedContent = string2bytes("AAA");
		final byte [] outdated = string2bytes("BBB");
		
		final Tag oldTag = new Tag(4,1);
		final Tag newTag = new Tag(5,1);
		
		// mock 1 - new version
		new Expectations() {{
			mockCli1.loadDoc(USER, DOC2);
			result = expectedContent;
		}};
		
		new Expectations() {{
			mockCli1.getSOAPtag();
			result = newTag;
		}};
		
		
		// mock 2 - old version
		new Expectations() {{
			mockCli2.loadDoc(USER, DOC2);
			result = outdated;
		}};
		
		new Expectations() {{
			mockCli1.getSOAPtag();
			result = oldTag;
		}};
		
		
		assertArrayEquals(expectedContent, _fe.loadDoc(USER, DOC2));
	}
	
	
	/**
	 * Teste para confirmar se resultado obtido equivale a resposta com TAG mais recente.
	 */
	@Test
	public void testTag2() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		final byte [] expected = string2bytes("ABC");
		final byte [] outdated = string2bytes("XYZ");
		
		final Tag oldTag = new Tag(4,1);
		final Tag newTag = new Tag(5,1);
		
		
		// mock1, old version
		new Expectations() {{
			mockCli1.loadDoc(USER, DOC2);
			result = outdated;
		}};
		
		new Expectations() {{
			mockCli1.getSOAPtag();
			result = oldTag;
		}};
		
		
		// mock2, new version
		new Expectations() {{
			mockCli2.loadDoc(USER, DOC2);
			result = expected;
		}};
		
		new Expectations() {{
			mockCli1.getSOAPtag();
			result = newTag;
		}};
		
		
		assertArrayEquals(expected, _fe.loadDoc(USER, DOC2));
	}
	
	/**
	 * Writeback devido a tags com versões diferentes.
	 */
	@Test
	public void writeBackVersion() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		final byte [] expected = string2bytes("ABC");
		final byte [] outdated = string2bytes("XYZ");
		
		final Tag oldTag = new Tag(4,1);
		final Tag newTag = new Tag(5,1);
		
		
		// mock 1 - load invocation + old version
		new Expectations() {{
			mockCli1.loadDoc(USER, DOC2);
			result = expected;
		}};
		
		new Expectations() {{
			mockCli1.getSOAPtag();
			result = oldTag;
		}};
		
		
		// mock 2 - load invocation + new version
		new Expectations() {{
			mockCli2.loadDoc(USER, DOC2);
			result = outdated;
		}};
		
		new Expectations() {{
			mockCli2.getSOAPtag();
			result = newTag;
		}};
		
		
		// write back
		new Expectations() {{
			mockCli1.storeDoc(USER, DOC2, expected);
		}};
		
		new Expectations() {{
			mockCli3.storeDoc(USER, DOC2, expected);
		}};
		
		
		assertArrayEquals(expected, _fe.loadDoc(USER, DOC2));
	}
	
	/**
	 * Writeback devido a tags com IDs diferentes.
	 */
	@Test
	public void writeBackFrontendID() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		final byte [] expected = string2bytes("ABC");
		final byte [] outdated = string2bytes("XYZ");
		
		final Tag lowID = new Tag(5,9000);
		final Tag highID = new Tag(5,9001);
		
		
		// mock 1 - load invocation + old version
		new Expectations() {{
			mockCli1.loadDoc(USER, DOC2);
			result = expected;
		}};
		
		new Expectations() {{
			mockCli1.getSOAPtag();
			result = lowID;
		}};
		
		
		// mock 2 - load invocation + new version
		new Expectations() {{
			mockCli2.loadDoc(USER, DOC2);
			result = outdated;
		}};
		
		new Expectations() {{
			mockCli2.getSOAPtag();
			result = highID;
		}};
		
		
		// write back
		new Expectations() {{
			mockCli1.storeDoc(USER, DOC2, expected);
		}};
		
		new Expectations() {{
			mockCli3.storeDoc(USER, DOC2, expected);
		}};
		
		
		assertArrayEquals(expected, _fe.loadDoc(USER, DOC2));
	}
	
	
	/*
	@Test
	public void loadWithQuorumVotes() 
			throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		
		new Expectations() {{
			mockCli1.loadDoc(USER, DOC);
			result = CONTENT;
		}};
		
		new Expectations() {{
			mockCli2.loadDoc(USER, DOC);
			result = CONTENT;
		}};	
		
		new Expectations() {{
			mockCli3.loadDoc(USER, DOC);
			result = string2bytes("OOPS, WRONG CONTENT");
		}};
		
		assertEquals(bytes2string(CONTENT), bytes2string(_fe.loadDoc(USER, DOC)));
	}
	
	
	@Test (expected = DocDoesNotExist_Exception.class)
	public void quorumWithDifferentExceptions() 
			throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		
		String message = "ERROR!";
		UserDoesNotExist udne = new UserDoesNotExist();
		udne.setUserId("Manuel");
		DocDoesNotExist ddne = new DocDoesNotExist();
		ddne.setDocId("DOC");
		
		final UserDoesNotExist_Exception UDNEex = new UserDoesNotExist_Exception(message, udne);
		final DocDoesNotExist_Exception DDNEex = new DocDoesNotExist_Exception(message, ddne);
		
		new Expectations() {{
			mockCli1.loadDoc(USER, DOC);
			result = UDNEex;
		}};
		
		new Expectations() {{
			mockCli2.loadDoc(USER, DOC);
			result = DDNEex;
		}};	
		
		new Expectations() {{
			mockCli3.loadDoc(USER, DOC);
			result = DDNEex;
		}};
		
		_fe.loadDoc(USER, DOC);
	}
	
	
	@Test
	public void noConsensus() 
			throws InvalidAttributeValueException, DocDoesNotExist_Exception, UserDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		
		new Expectations() {{
			mockCli1.loadDoc(USER, DOC);
			result = string2bytes("HEY");
		}};
		
		new Expectations() {{
			mockCli2.loadDoc(USER, DOC);
			result = string2bytes("HELLO");
		}};	
		
		new Expectations() {{
			mockCli3.loadDoc(USER, DOC);
			result = string2bytes("BYE");
		}};
		
		_fe.loadDoc(USER, DOC);
	}
	
	
	
	
	@Test
	public void writeBack1() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		new Expectations() {{
			mockCli1.loadDoc(USER, DOC);
			result = CONTENT;
		}};
		
		new Expectations() {{
			mockCli2.loadDoc(USER, DOC);
			result = string2bytes("WRONG CONTENT");
		}};	
		
		new Expectations() {{
			mockCli3.loadDoc(USER, DOC);
			result = CONTENT;
		}};
		
		new Expectations() {{
			mockCli2.storeDoc(USER, DOC, CONTENT);
		}};
		
		byte[] result = _fe.loadDoc(USER, DOC);
		
		assertEquals(result, CONTENT);
	}
	
	@Test
	public void writeBack2() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception, NoConsensusException{
		String message = "ERROR!";
		UserDoesNotExist uddne = new UserDoesNotExist();
		uddne.setUserId("Manuel");
		final UserDoesNotExist_Exception UDNEex = new UserDoesNotExist_Exception(message, uddne);
		
		new Expectations() {{
			mockCli1.loadDoc(USER, DOC);
			result = UDNEex;
		}};
		
		new Expectations() {{
			mockCli2.loadDoc(USER, DOC);
			result = CONTENT;
		}};	
		
		new Expectations() {{
			mockCli3.loadDoc(USER, DOC);
			result = CONTENT;
		}};
		
		new Expectations() {{
			mockCli1.storeDoc(USER, DOC, CONTENT);
		}};
		
		byte[] result = _fe.loadDoc(USER, DOC);
		assertEquals(result, CONTENT);
	}
	
	*/
}
