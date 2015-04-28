package pt.tecnico.bubbledocs.service.remote;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.cli.StoreClient;

public class StoreRemoteServicesTest {
	public String USER = "duarte";
	public String DOC = "SD-notes";
	public String CONTENT = "O importante é distribuir bem o jogo, neste caso o sistema.";
	
	@Test
	public void storeAndLoad(){
		StoreRemoteServices serv = new StoreRemoteServices();
		
		byte[] content = StoreClient.string2bytes(CONTENT);
		serv.storeDocument(USER, DOC, content);
		
		byte[] result = serv.loadDocument(USER, DOC);
		
		assertEquals(content, result);
	}
	
}
