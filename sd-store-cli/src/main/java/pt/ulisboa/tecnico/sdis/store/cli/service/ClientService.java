package pt.ulisboa.tecnico.sdis.store.cli.service;

import java.util.Map;

import javax.crypto.SecretKey;
import javax.xml.ws.BindingProvider;

import pt.tecnico.sd.SdCrypto;
import pt.ulisboa.tecnico.sdis.store.cli.ClientHeaderHandler;
import pt.ulisboa.tecnico.sdis.store.cli.StoreClient;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;

public abstract class ClientService {
	public static final String CLASS_NAME = StoreClient.class.getSimpleName();
	
	protected SDStore port;
	
	public ClientService(SDStore port){
		this.port = port;
	}
	
	
	public final void addMacDigest(byte[] content){
		byte[] bytes4mac = StoreClient.string2bytes("TESTEMAC");
		SecretKey macKey = SdCrypto.generateMacKey(bytes4mac);
		
		byte[] macBytes;
		macBytes = SdCrypto.produceMac(content, macKey);
		
		// FIXME: this produces a runtime error on local tests
		//addMACtoSOAP(macBytes);
		
	}
	
	public final void addMACtoSOAP(byte[] MAC){
		//TODO: add mac to SOAP message, somehow
		BindingProvider bindingProvider = (BindingProvider) port;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        
        String initialValue = StoreClient.bytes2string(MAC);
        System.out.printf("%s put token '%s' on request context%n", CLASS_NAME, initialValue);
        requestContext.put(ClientHeaderHandler.STORE_CONTENT_MAC, initialValue);
	}
}
