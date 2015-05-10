package pt.ulisboa.tecnico.sdis.store.cli.service;

import java.util.Map;

import javax.crypto.SecretKey;
import javax.xml.ws.BindingProvider;

import pt.tecnico.sd.SdCrypto;
import pt.ulisboa.tecnico.sdis.store.cli.StoreClient;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;

public abstract class ClientService {
	protected SDStore port;
	
	public ClientService(SDStore port){
		this.port = port;
	}
	
	
	public final void addMacDigest(byte[] content){
		byte[] bytes4mac = StoreClient.string2bytes("TESTEMAC");
		SecretKey macKey = SdCrypto.generateMacKey(bytes4mac);
		
		byte[] macBytes;
		macBytes = SdCrypto.produceMac(content, macKey);
		
		addMACtoSOAP(macBytes);
		
	}
	
	public final void addMACtoSOAP(byte[] MAC){
		//TODO: add mac to SOAP message, somehow
		BindingProvider bindingProvider = (BindingProvider) port;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
	}
}
