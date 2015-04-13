package pt.ulisboa.tecnico.sdis.store.tests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import pt.ulisboa.tecnico.sdis.store.cli.StoreClient;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;

public class ClientStoreServicesTest {
	protected SDStore _port;
	
	private final String USER = "duarte";
	private final String DOC_STORED = "my first novel";
	private final String DOC_NOT_STORED = "my second novel";
	private final String CONTENT = "This is a short story. THE END.";
	
	@BeforeClass
	public void populateOnce(){
		
	}
	
	@AfterClass
	public void finalDestroy(){
		
	}
	
	@Before
	public void populate4Test(){
		_port = StoreClient.initPort();
	}
	
	@After
	public void destroyAfterTest(){
		
	}
	
	public String bytes2String(byte[] bytes){
		return StoreClient.bytes2string(bytes);
	}
	
	public byte[] string2bytes(String s){
		return StoreClient.string2bytes(s);
	}
}
