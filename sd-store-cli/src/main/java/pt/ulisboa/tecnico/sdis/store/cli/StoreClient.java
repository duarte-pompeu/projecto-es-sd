package pt.ulisboa.tecnico.sdis.store.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.naming.directory.InvalidAttributeValueException;
import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.juddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.store.cli.service.CreateDocService;
import pt.ulisboa.tecnico.sdis.store.cli.service.ListDocsService;
import pt.ulisboa.tecnico.sdis.store.cli.service.LoadDocService;
import pt.ulisboa.tecnico.sdis.store.cli.service.StoreDocService;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class StoreClient{
	
	public static final boolean VERBOSE = true;
	public static final boolean ENCRYPTION = true;
	
	// default values
	public static final String DEFAULT_UDDI_URL = "http://localhost:8081";
	public static final String DEFAULT_UDDI_NAME = "sd-store";
	
	public String uddiURL;
	public String uddiName;
	private SDStore _port = null;
	
	private Key key;
	public Cipher encrypter;
	public Cipher decrypter;
	
	
	public StoreClient() throws JAXRException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException{
		this(DEFAULT_UDDI_URL, DEFAULT_UDDI_NAME);
	}
	
	
	public StoreClient(String uddiUrl, String uddiName) throws JAXRException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException{
		this.uddiURL = uddiUrl;
		this.uddiName = uddiName;
		
		initCrypto();
		_port = findUddi(uddiURL, uddiName);
	}
	
	
	public static SDStore createPort(){
		SDStore_Service service = new SDStore_Service();
		return service.getSDStoreImplPort();
	}
	
	public SDStore getPort(){
		return _port;
	}
	
	
	public void initIfNoPort(){
		if(_port == null){
			try {
				_port = findUddi(uddiURL, uddiName);
			} catch (JAXRException e) {
				return;
			}
		}
	}
	
	
	public SDStore findUddi(String uddiURL, String uddiName) throws JAXRException {
		SDStore port;
		System.out.printf("Contacting UDDI at %s%n", uddiURL);
        UDDINaming uddiNaming = new UDDINaming(uddiURL);
        
        System.out.printf("Looking for '%s'%n", uddiName);
        String endpointAddress = uddiNaming.lookup(uddiName);
        
        if (endpointAddress == null) {
            System.out.println("Not found!");
            return null;
        } else {
            System.out.printf("Found %s%n", endpointAddress);
        }
        
        System.out.println("Creating stub ...");
        SDStore_Service service = new SDStore_Service();
        port = service.getSDStoreImplPort();
        
        System.out.println("Setting endpoint address ...");
        BindingProvider bindingProvider = (BindingProvider) port;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
        
        return port;
	}
	
	public void initCrypto() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
		this.key = getKey();
		
		this.encrypter = Cipher.getInstance("DES/ECB/PKCS5Padding");
		encrypter.init(Cipher.ENCRYPT_MODE, key);
		
		this.decrypter = Cipher.getInstance("DES/ECB/PKCS5Padding");
		decrypter.init(Cipher.DECRYPT_MODE, key);
	}
	
	
	public void createDoc(String userID, String docID) throws InvalidAttributeValueException, DocAlreadyExists_Exception{
		CreateDocService service = new CreateDocService(userID, docID, _port);
		service.dispatch();
	}
	
	
	public List<String> listDocs(String userID) throws InvalidAttributeValueException, UserDoesNotExist_Exception{
		ListDocsService service = new ListDocsService(userID, _port);
		service.dispatch();
		return service.getResult();
	}
	
	
	public byte[] loadDoc(String userID, String docID) throws InvalidAttributeValueException, DocDoesNotExist_Exception, UserDoesNotExist_Exception{
		LoadDocService service = new LoadDocService(userID, docID, _port);
		service.dispatch();
		
		if(!ENCRYPTION){
			return service.getResult();
		}
		
		
		byte[] ciphered = service.getResult();
		byte[] plainBytes = null;
		
		try {
			plainBytes = decrypter.doFinal(ciphered);
		} 
		catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		
		if(VERBOSE){
			System.out.println("ENCRYPTED: " + bytes2string(ciphered));
			System.out.println("DECRYPTED: " + bytes2string(plainBytes));
		}
		
		return plainBytes;
	}
	
	
	public void storeDoc(String userID, String docID, byte[] content) throws InvalidAttributeValueException, CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception{
		byte[] bytes2upload = content;
		
		if(ENCRYPTION){
			try {
				bytes2upload = encrypter.doFinal(content);
			} 
			catch (IllegalBlockSizeException | BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		StoreDocService service = new StoreDocService(userID, docID, bytes2upload, _port);
		service.dispatch();
		
	}

	
	public static byte[] string2bytes(String s){
		try {
			return s.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	
	public static String bytes2string(byte[] bytes){
		try {
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	
	public Key getKey(){
		KeyGenerator keyGen = null;
		
		try {
			keyGen = KeyGenerator.getInstance("DES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		keyGen.init(56);
		return keyGen.generateKey();
	}
}
