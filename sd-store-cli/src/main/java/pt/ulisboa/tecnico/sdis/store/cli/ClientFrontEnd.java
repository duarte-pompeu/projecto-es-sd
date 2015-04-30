package pt.ulisboa.tecnico.sdis.store.cli;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;
import javax.xml.registry.JAXRException;

public class ClientFrontEnd {
	private static StoreClient _client = null;
	
	public static StoreClient getClientInstance(){
		if(_client == null){
			
			try {
				_client = new StoreClient();
			} 
			catch (InvalidKeyException | NoSuchAlgorithmException
					| NoSuchPaddingException | JAXRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return _client;
	}
}
