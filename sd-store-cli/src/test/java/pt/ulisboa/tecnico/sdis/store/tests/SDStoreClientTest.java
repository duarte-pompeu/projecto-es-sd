package pt.ulisboa.tecnico.sdis.store.tests;

import pt.ulisboa.tecnico.sdis.store.cli.StoreClient;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
/**
 * This superclass has some useful attributes and methods its subclasses can use without re-implementing.
 */
public class SDStoreClientTest {
	protected static SDStore _port;

	
	public static String bytes2string(byte[] bytes){
		return StoreClient.bytes2string(bytes);
	}
	
	
	public static byte[] string2bytes(String s){
		return StoreClient.string2bytes(s);
	}
	
	
	public SDStore getPort(){
		return _port;
	}
}
