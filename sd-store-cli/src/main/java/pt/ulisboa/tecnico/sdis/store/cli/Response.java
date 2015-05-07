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
	private InvalidAttributeValueException iaevEx = null;
	private DocDoesNotExist_Exception ddneEx = null;
	private UserDoesNotExist_Exception udneEx = null;
	
	
	
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
		this.iaevEx = e;
	}


	public Response(DocDoesNotExist_Exception e) {
		TYPE = DDNE_EXCEPTION;
		this.ddneEx = e;
	}


	public Response(UserDoesNotExist_Exception e) {
		TYPE = UDNE_EXCEPTION;
		this.udneEx = e;
	}


	public boolean equals(Response r){
		return this.TYPE == r.TYPE 
				&& rEquals(this.docContent, r.docContent)
				&& rEquals(this.except, r.except)
				&& rEquals(this.iaevEx, r.iaevEx)
				&& rEquals(this.ddneEx, r.ddneEx)
				&& rEquals(this.udneEx, r.udneEx);
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
	
	
	public boolean rEquals(UserDoesNotExist_Exception e1, UserDoesNotExist_Exception e2){
		if(e1 == null || e2 == null){
			return (e1 == null && e2 == null);
		}
		
		return e1.getMessage().equals(e2.getMessage())
				&& e1.getFaultInfo().getUserId().equals(e2.getFaultInfo().getUserId());
	}
	
	
	public boolean rEquals(DocDoesNotExist_Exception e1, DocDoesNotExist_Exception e2){
		if(e1 == null || e2 == null){
			return (e1 == null && e2 == null);
		}
		
		return e1.getMessage().equals(e2.getMessage())
				&& e1.getFaultInfo().getDocId().equals(e2.getFaultInfo().getDocId());
	}


	public byte[] getContent() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception {
		int type = this.TYPE;
		
		switch(type){
//		case GENERIC_EXCEPTION:
//			if(except == null)
//				return null;
//			
//			throw except;
			
		case IAV_EXCEPTION:
			if(iaevEx == null)
				return null;
			
			throw iaevEx;
		
		case DDNE_EXCEPTION:
			if(ddneEx == null)
				return null;
		
			throw ddneEx;
		
		case UDNE_EXCEPTION:
			if(udneEx == null)
				return null;
			
			throw udneEx;
		}
		
		return docContent;
	}
}
