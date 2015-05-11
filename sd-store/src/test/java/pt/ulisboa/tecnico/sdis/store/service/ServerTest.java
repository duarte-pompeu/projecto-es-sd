package pt.ulisboa.tecnico.sdis.store.service;


/**
 * This class contains some useful methods that other tests may use.
 */
public class ServerTest {

	public byte[] string2bytes(String s){
		return SDStoreService.string2bytes(s);
	}
	
	
	public String bytes2string(byte[] bytes){
		return SDStoreService.bytes2string(bytes);
	}
}
