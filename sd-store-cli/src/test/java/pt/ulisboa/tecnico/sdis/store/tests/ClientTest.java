package pt.ulisboa.tecnico.sdis.store.tests;

import javax.xml.registry.JAXRException;

import org.junit.BeforeClass;

import pt.ulisboa.tecnico.sdis.store.cli.StoreClient;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
/**
 * This superclass has some useful attributes and methods its subclasses can use without re-implementing.
 */
public class ClientTest {
	protected static SDStore _port;

	
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
