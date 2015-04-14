package pt.ulisboa.tecnico.sdis.store.tests;

import javax.xml.registry.JAXRException;

import org.junit.After;
import org.junit.Before;

import pt.ulisboa.tecnico.sdis.store.cli.StoreClient;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;

public class ClientTest {
	protected SDStore _port;
	
	public ClientTest() throws JAXRException{
		_port = StoreClient.initPort();
		//StoreClient.findUddi("http://localhost:8081", "SDStore");
	}
	
	
	@Before
	public void populate4Test(){
	
	}
	
	
	@After
	public void destroyAfterTest(){
		
	}
	
	
	public String bytes2string(byte[] bytes){
		return StoreClient.bytes2string(bytes);
	}
	
	
	public byte[] string2bytes(String s){
		return StoreClient.string2bytes(s);
	}
	
	
	public SDStore getPort(){
		return _port;
	}
}
