package pt.ulisboa.tecnico.sdis.store.cli.service;

import javax.crypto.SecretKey;

import pt.tecnico.sd.SdCrypto;
import pt.ulisboa.tecnico.sdis.store.cli.StoreClient;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;

public abstract class ClientService {
	public static final String CLASS_NAME = StoreClient.class.getSimpleName();
	
	protected SDStore port;
	protected byte[] MAC;
	
	public ClientService(SDStore port){
		this.port = port;
	}
	
	public byte[] getMAC(){
		return MAC;
	}
	
	
	public final void addMacDigest(byte[] content){
		byte[] bytes4mac = StoreClient.string2bytes("TESTEMAC");
		SecretKey macKey = SdCrypto.generateMacKey(bytes4mac);
		
		byte[] macBytes;
		macBytes = SdCrypto.produceMac(content, macKey);
		
		MAC = macBytes;
	}
	
	public void afterDispatch(){
		//StoreClient.MAC = null;
	}
}
