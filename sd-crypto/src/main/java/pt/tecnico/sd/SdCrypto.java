package pt.tecnico.sd;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class SdCrypto {
	
	public static void main(String[] args) throws Exception {
		byte[] password = new String("hunter2").getBytes();
		System.out.println("Password: hunter2");
		
		byte[] digest = digestPassword(password);
		System.out.println("Digest: " + printHexBinary(digest));
		
		//Criar a chave a partir da password:
		SecretKey key = generateKey(digest);
		System.out.println("Key:    " + printHexBinary(key.getEncoded()));
		
		//Plaintext
		String plaintext = "attack at midnight";
		System.out.println("Plaintext: " + plaintext);
		
		//Ciphertext
		byte[] encrypted = encrypt(key, plaintext.getBytes());
		
		System.out.println("Ciphered: " + printHexBinary(encrypted));
		
		
		System.out.println("Deciphered: " + new String(decrypt(key, encrypted)));
	}
	
	public static byte[] digestPassword(byte[] password) {
		MessageDigest digest;
		
		try {
			digest = MessageDigest.getInstance("SHA-256");
			digest.update(password);
			return digest.digest();
			
		} catch (NoSuchAlgorithmException e) {
			// This should never happen
		}
		
		return null;
	}
	
	public static SecretKey generateKey(byte[] digest) throws InvalidKeyException {
		return new SecretKeySpec((new DESedeKeySpec(digest).getKey()), "DESede");
	}
	
	public static byte[] encrypt(SecretKey key, byte[] plaintext) {
		try {
			Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(plaintext);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static byte[] decrypt(SecretKey key, byte[] encrypted) {
		try {
			Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(encrypted);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
