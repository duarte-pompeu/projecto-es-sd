package pt.ulisboa.tecnico.sdis.store.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import javax.naming.directory.InvalidAttributeValueException;

import org.junit.BeforeClass;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.cli.Response;
import pt.ulisboa.tecnico.sdis.store.cli.StoreClient;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class ResponseTest {
	static byte[] CONTENT;
	static byte[] CONTENT_REPLICA;
	static byte[] CONTENT_ALT;
	
	static Exception GENERIC_EXCEPTION;
	static Exception GENERIC_EXCEPTION_REPLICA;
	static Exception GENERIC_EXCEPTION_ALT;
	
	static InvalidAttributeValueException IAVEx;
	static InvalidAttributeValueException IAVEx_REPLICA;
	static InvalidAttributeValueException IAVEx_ALT;
	
	static DocDoesNotExist_Exception DDNEEx;
	static DocDoesNotExist_Exception DDNEEx_REPLICA;
	static DocDoesNotExist_Exception DDNEEx_ALT;
	
	static UserDoesNotExist_Exception UDNEEx;
	static UserDoesNotExist_Exception UDNEEx_REPLICA;
	static UserDoesNotExist_Exception UDNEEx_ALT;
	
	
	@BeforeClass
	public static void init(){
		CONTENT = StoreClient.string2bytes("LOREM IPSUM");
		CONTENT_REPLICA = StoreClient.string2bytes("LOREM IPSUM");
		CONTENT_ALT = StoreClient.string2bytes("IPSUM LOREM");
		
		
		GENERIC_EXCEPTION = new RuntimeException("HELLO");
		GENERIC_EXCEPTION_REPLICA = new RuntimeException("HELLO");
		GENERIC_EXCEPTION_ALT = new RuntimeException("GOODBYE");
		
		
		IAVEx = new InvalidAttributeValueException("no explanation");
		IAVEx_REPLICA = new InvalidAttributeValueException("no explanation");
		IAVEx_ALT = new InvalidAttributeValueException("cant explain this");
		
		
		String message = "no such thing";
		
		DocDoesNotExist ddne = new DocDoesNotExist();
		DocDoesNotExist ddne_copy = new DocDoesNotExist();
		DocDoesNotExist ddne_alt = new DocDoesNotExist();
		
		ddne.setDocId("123");
		ddne_copy.setDocId("123");
		ddne_alt.setDocId("123456789");
		
		DDNEEx = new DocDoesNotExist_Exception(message, ddne);
		DDNEEx_REPLICA = new DocDoesNotExist_Exception(message, ddne_copy);
		DDNEEx_REPLICA = new DocDoesNotExist_Exception(message, ddne_alt);
		
		
		message = "no such user";
		
		UserDoesNotExist uddne = new UserDoesNotExist();
		UserDoesNotExist uddne_copy = new UserDoesNotExist();
		UserDoesNotExist uddne_alt = new UserDoesNotExist();
		
		uddne.setUserId("Manuel");
		uddne_copy.setUserId("Manuel");
		uddne_alt.setUserId("Joaquim");
		
		UDNEEx = new UserDoesNotExist_Exception(message, uddne);
		UDNEEx_REPLICA = new UserDoesNotExist_Exception(message, uddne_copy);
		UDNEEx_ALT = new UserDoesNotExist_Exception(message, uddne_alt);
	}
	
	
	@Test
	public void differentTypesOfResponsesDiffer(){
		ArrayList<Response> rlist = new ArrayList<Response>();
		
		Response r1 = new Response(CONTENT); 
		rlist.add(r1);
		Response r2 = new Response(GENERIC_EXCEPTION); 
		rlist.add(r2);
		Response r3 = new Response(IAVEx); 
		rlist.add(r3);
		Response r4 = new Response(DDNEEx); 
		rlist.add(r4);
		Response r5 = new Response(UDNEEx); 
		rlist.add(r5);
		
		
		Response ri;
		Response rj;
		
		for(int i = 0; i < rlist.size(); i++){
			ri = rlist.get(i);
			
			for(int j = i+1; j < rlist.size(); j++){
				rj = rlist.get(j);
				
				assertFalse(ri.equals(rj));
			}
		}
	}
	
	
	@Test
	public void contentEquals(){
		Response r1 = new Response(CONTENT);
		Response r2 = new Response(CONTENT_REPLICA);
		
		assertTrue(r1.equals(r2));
	}
	
	
	@Test
	public void contentDiffers(){
		Response r1 = new Response(CONTENT);
		Response r2 = new Response(CONTENT_ALT);
		
		assertFalse(r1.equals(r2));
	}
	
	
	@Test
	public void genericExceptionEquals(){
		Response r1 = new Response(GENERIC_EXCEPTION);
		Response r2 = new Response(GENERIC_EXCEPTION_REPLICA);
		
		assertTrue(r1.equals(r2));
	}
	
	
	@Test
	public void genericExceptionDiffers(){
		Response r1 = new Response(GENERIC_EXCEPTION);
		Response r2 = new Response(GENERIC_EXCEPTION_ALT);
		
		assertFalse(r1.equals(r2));
	}
	
	
	@Test
	public void iavexEquals(){
		Response r1 = new Response(IAVEx);
		Response r2 = new Response(IAVEx_REPLICA);
		
		assertTrue(r1.equals(r2));
	}
	
	
	@Test
	public void iavExDiffers(){
		Response r1 = new Response(IAVEx);
		Response r2 = new Response(IAVEx_ALT);
		
		assertFalse(r1.equals(r2));
	}
	
	
	@Test
	public void ddneExEquals(){
		Response r1 = new Response(DDNEEx);
		Response r2 = new Response(DDNEEx_REPLICA);
		
		assertTrue(r1.equals(r2));
	}
	
	
	@Test
	public void ddneExDiffers(){
		Response r1 = new Response(DDNEEx);
		Response r2 = new Response(DDNEEx_ALT);
		
		assertFalse(r1.equals(r2));
	}
	
	
	@Test
	public void udneExEquals(){
		Response r1 = new Response(UDNEEx);
		Response r2 = new Response(UDNEEx_REPLICA);
		
		assertTrue(r1.equals(r2));
	}
	
	
	@Test
	public void udneExDiffers(){
		Response r1 = new Response(UDNEEx);
		Response r2 = new Response(UDNEEx_ALT);
		
		assertFalse(r1.equals(r2));
	}
	
}
