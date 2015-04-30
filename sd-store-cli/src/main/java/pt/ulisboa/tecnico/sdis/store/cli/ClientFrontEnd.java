package pt.ulisboa.tecnico.sdis.store.cli;

import javax.xml.registry.JAXRException;

public class ClientFrontEnd {
	private static StoreClient _client = null;
	
	public static StoreClient getClientInstance() throws JAXRException{
		if(_client == null){
			_client = new StoreClient();
		}
		
		return _client;
	}
}
