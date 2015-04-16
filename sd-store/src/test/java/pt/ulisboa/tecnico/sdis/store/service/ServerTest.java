package pt.ulisboa.tecnico.sdis.store.service;

import pt.ulisboa.tecnico.sdis.store.ws.SDStoreMain;

/**
 * This class contains some useful methods that other tests may use.
 */
public class ServerTest {

	public byte[] string2bytes(String s){
		return SDStoreMain.string2bytes(s);
	}
	
	
	public String bytes2string(byte[] bytes){
		return SDStoreMain.bytes2string(bytes);
	}
}
