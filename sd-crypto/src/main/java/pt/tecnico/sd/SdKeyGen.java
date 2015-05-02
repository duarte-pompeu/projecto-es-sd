package pt.tecnico.sd;

import java.io.FileOutputStream;
import java.security.Key;
import java.security.MessageDigest;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

public class SdKeyGen {

	//Generate a key for 3DES	
	public static void main(String[] args) throws Exception {
		System.out.println("Generating 3DES key ..." );
        
		KeyGenerator keygen = KeyGenerator.getInstance("DESede");
        keygen.init(168);
        Key key = keygen.generateKey();
        
        System.out.println("Finished generating 3DES key!");
        
        byte[] encoded = key.getEncoded();
        System.out.printf("Key: %s%n", printHexBinary(encoded));
        
        System.out.println("Writing key to files");	
        
        //This folder
        FileOutputStream out = new FileOutputStream("secret-key");
        //sd-id
        FileOutputStream sdid = new FileOutputStream("../sd-id/secret-key");
        //sd-store
        FileOutputStream sdstore = new FileOutputStream("../sd-store/secret-key");

        try {
        	out.write(encoded);
        	sdid.write(encoded);
        	sdstore.write(encoded);
        } catch (Exception e) {throw e;}
        finally {
        	out.close();
        	sdid.close();
        	sdstore.close();
        }
        
        System.out.println("Done. Shh, keep a secret!");       
        
	}
}
