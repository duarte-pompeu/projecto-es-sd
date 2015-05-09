package pt.tecnico.sd;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.DESKeySpec;
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
			throw new RuntimeException(e);
		}
	}
	
	public static SecretKey generateRandomKey() {
		try {
			KeyGenerator keygen = KeyGenerator.getInstance("DESede");
			keygen.init(168);
			return keygen.generateKey();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static SecretKey generateKey(byte[] digest)  {
		try {
			return new SecretKeySpec((new DESedeKeySpec(digest).getKey()), "DESede");
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		}
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
	
	public static SecretKey generateRandomMacKey() {
		try {
			KeyGenerator keygen = KeyGenerator.getInstance("DES");
			keygen.init(56);
			return keygen.generateKey();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static SecretKey generateMacKey(byte[] macKey) {
		try {
			return new SecretKeySpec((new DESKeySpec(macKey).getKey()), "DES");
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static byte[] produceMac(byte[] data, SecretKey key) {

		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5"); 
			messageDigest.update(data);
			byte[] digest = messageDigest.digest();

			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(digest);
			
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static boolean verifyMac(byte[] data, byte[] mac) {
		//TODO
		return false;
	}
}
