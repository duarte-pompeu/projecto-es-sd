package pt.ulisboa.tecnico.sdis.store.cli;

import java.util.Collection;
import java.util.Iterator;

import javax.naming.directory.InvalidAttributeValueException;

import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class Response {
	private static final int SUCCESS = 0;
	private static final int CONTENT = 100;
	private static final int NAME_CONTENT = 101;
	private static final int GENERIC_EXCEPTION = 200;
	private static final int IAV_EXCEPTION = 201;
	private static final int DDNE_EXCEPTION = 202;
	private static final int UDNE_EXCEPTION = 203;
	private static final int CE_EXCEPTION = 204;
	
	public final int TYPE;
	private Tag tag;
	
	private byte[] docContent = null;
	private Collection<String> docNameCollection = null;
	private Exception except = null;
	private InvalidAttributeValueException iaevEx = null;
	private DocDoesNotExist_Exception ddneEx = null;
	private UserDoesNotExist_Exception udneEx = null;
	private CapacityExceeded_Exception ceEx = null;
	
	
	public final int ID;
	
	public Response(int ID) {
		TYPE = SUCCESS;
		this.ID = ID;
	}
	
	
	public Response(byte[] content, int ID){
		TYPE = CONTENT;
		docContent = content;
		this.ID = ID;
		
	}
	
	
	public Response(Exception e, int ID){
		TYPE = GENERIC_EXCEPTION;
		this.except = e;
		this.ID = ID;
	}
	

	public Response(InvalidAttributeValueException e, int ID) {
		TYPE = IAV_EXCEPTION;
		this.iaevEx = e;
		this.ID = ID;
	}


	public Response(DocDoesNotExist_Exception e, int ID) {
		TYPE = DDNE_EXCEPTION;
		this.ddneEx = e;
		this.ID = ID;
	}


	public Response(UserDoesNotExist_Exception e, int ID) {
		TYPE = UDNE_EXCEPTION;
		this.udneEx = e;
		this.ID = ID;
	}


	public Response(CapacityExceeded_Exception e, int ID){
		TYPE = CE_EXCEPTION;
		this.ceEx = e;
		this.ID = ID;
	}
	
	public Response(Collection<String> docNames, int ID){
		TYPE = NAME_CONTENT;
		docNameCollection = docNames;
		this.ID = ID;
	}

	public boolean equals(Response r){
		return this.TYPE == r.TYPE 
				&& rEquals(this.docContent, r.docContent)
				&& rEquals(this.except, r.except)
				&& rEquals(this.iaevEx, r.iaevEx)
				&& rEquals(this.ddneEx, r.ddneEx)
				&& rEquals(this.udneEx, r.udneEx)
				&& rEquals(this.ceEx, r.ceEx);
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
	
	
	public static boolean rEquals(Collection<String> ba1, Collection<String> ba2){
		if(ba1 == null || ba2 == null){
			return (ba1 == null && ba2 == null);
		}
		
		if(ba1.size() != ba2.size()){
			return false;
		}
		Iterator<String> it1=ba1.iterator();
		Iterator<String> it2=ba2.iterator();
		for(int i = 0; i < ba1.size();i++, it1.next(), it2.next()){
			if(!it1.equals(it2)){
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


	public byte[] getContent() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception {
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
			
		case CE_EXCEPTION:
			if(ceEx == null)
				return null;
			
			throw ceEx;
		}
		
		
		
		return docContent;
	}
	
	public Collection<String> getDocNames() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception {
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
			
		case CE_EXCEPTION:
			if(ceEx == null)
				return null;
			
			throw ceEx;
		}
		
		return docNameCollection;
	
	}


	public void setTag(Tag tag) {
		this.tag = tag;
	}
}
