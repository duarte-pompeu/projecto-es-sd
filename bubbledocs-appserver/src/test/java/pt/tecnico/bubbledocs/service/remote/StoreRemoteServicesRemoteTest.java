package pt.tecnico.bubbledocs.service.remote;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.cli.StoreClient;

public class StoreRemoteServicesRemoteTest {
	static StoreRemoteServices storeRemote;
	
	public String USER = "duarte";
	public String DOC = "SD-notes";
	public String CONTENT = "O importante é distribuir bem o jogo, neste caso o sistema.";
	
	@BeforeClass
	public static void init(){
		storeRemote = new StoreRemoteServices();
	}
	
	
	@Test
	public void storeAndLoad(){
		byte[] content = StoreClient.string2bytes(CONTENT);
		storeRemote.storeDocument(USER, DOC, content);
		
		byte[] result = storeRemote.loadDocument(USER, DOC);
		
		assertEquals(content, result);
	}
	
}
