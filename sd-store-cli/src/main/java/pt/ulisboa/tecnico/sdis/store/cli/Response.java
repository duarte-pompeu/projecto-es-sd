package pt.ulisboa.tecnico.sdis.store.cli;

import javax.naming.directory.InvalidAttributeValueException;

import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class Response {
	private static final int CONTENT = 100;
	private static final int GENERIC_EXCEPTION = 200;
	private static final int IAV_EXCEPTION = 201;
	private static final int DDNE_EXCEPTION = 202;
	private static final int UDNE_EXCEPTION = 203;
	
	public final int TYPE;
	private byte[] docContent = null;
	private Exception except = null;
	private InvalidAttributeValueException iaeve = null;
	private DocDoesNotExist_Exception ddnee = null;
	private UserDoesNotExist_Exception udnee = null;
	
	
	
	public Response(byte[] content){
		TYPE = CONTENT;
		docContent = content;
	}
	
	public Response(Exception e){
		TYPE = GENERIC_EXCEPTION;
		this.except = e;
	}
	

	public Response(InvalidAttributeValueException e) {
		TYPE = IAV_EXCEPTION;
		this.iaeve = e;
	}


	public Response(DocDoesNotExist_Exception e) {
		TYPE = DDNE_EXCEPTION;
		this.ddnee = e;
	}


	public Response(UserDoesNotExist_Exception e) {
		TYPE = UDNE_EXCEPTION;
		this.udnee = e;
	}


	public boolean equals(Response r){
		return this.TYPE == r.TYPE 
				&& rEquals(this.docContent, r.docContent)
				&& rEquals(this.except, r.except);
	}
	
	
	public static boolean rEquals(byte[] ba1, byte[] ba2){
		if(ba1 == null || ba2 == null){
			return (ba1 == null && ba2 == null);
		}
		
		if(ba1.length != ba2.length){
			return false;
		}
		
		for(int i = 0; i < ba1.length; i++){
			if(ba1[i] != ba2[i]){
				return false;
			}
		}
		
		return true;
	}
	
	
	public boolean rEquals(Exception e1, Exception e2){
		if(e1 == null || e2 == null){
			return (e1 == null && e2 == null);
		}
		
		return e1.getMessage().equals(e2.getMessage());
	}


	public byte[] getContent() {
		return docContent;
	}
}
