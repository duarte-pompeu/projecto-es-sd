package pt.ulisboa.tecnico.sdis.store.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.juddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;

public class StoreClient{
	static SDStore _port;
	
	// default values
	public static String uddiURL = "http://localhost:8081";
	public static String uddiName ="sd-store";
	
	
	public static void main(String[] args){

		if(args.length >= 1){
			uddiURL = args[0];
		}
		
		if(args.length >= 2){
			uddiName = args[1];
		}
		
		
		try {
			_port = findUddi(uddiURL, uddiName);
		} catch (JAXRException e) {
			System.err.println("Connection failed: exception raised when checking UDDI.");
			System.err.println(e.getMessage());
		}
	}
	
	
	public static SDStore findUddi(String uddiURL, String uddiName) throws JAXRException {
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
	
	public static SDStore initPort(){
		SDStore_Service service = new SDStore_Service();
		return service.getSDStoreImplPort();
	}
}
