package pt.ulisboa.tecnico.sdis.store.localtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.naming.directory.InvalidAttributeValueException;

import org.junit.BeforeClass;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.cli.Response;
import pt.ulisboa.tecnico.sdis.store.cli.StoreClient;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

/**
 * Esta classe testava exaustivamente o antigo comportamento da classe Response.
 * 
 * Com a simplicação do uso de Response, o nº de testes foi reduzido.
 * Os testes antigos estão comentados, para referência.
 */
public class ResponseTest {
	static byte[] CONTENT;
	static byte[] CONTENT_COPY;
	static byte[] CONTENT_ALT;
	
	static Exception GENERIC_EXCEPTION;
	static Exception GENERIC_EXCEPTION_COPY;
	static Exception GENERIC_EXCEPTION_ALT;
	
	static InvalidAttributeValueException IAVex;
	static InvalidAttributeValueException IAVex_COPY;
	static InvalidAttributeValueException IAVex_ALT;
	
	static DocDoesNotExist_Exception DDNEex;
	static DocDoesNotExist_Exception DDNEex_COPY;
	static DocDoesNotExist_Exception DDNEex_ALT;
	
	static UserDoesNotExist_Exception UDNEex;
	static UserDoesNotExist_Exception UDNEex_COPY;
	static UserDoesNotExist_Exception UDNEex_ALT;
	
	
	@BeforeClass
	public static void init(){
		CONTENT = StoreClient.string2bytes("LOREM IPSUM");
		CONTENT_COPY = StoreClient.string2bytes("LOREM IPSUM");
		CONTENT_ALT = StoreClient.string2bytes("IPSUM LOREM");
		
		
		GENERIC_EXCEPTION = new RuntimeException("HELLO");
		GENERIC_EXCEPTION_COPY = new RuntimeException("HELLO");
		GENERIC_EXCEPTION_ALT = new RuntimeException("GOODBYE");
		
		
		IAVex = new InvalidAttributeValueException("no explanation");
		IAVex_COPY = new InvalidAttributeValueException("no explanation");
		IAVex_ALT = new InvalidAttributeValueException("cant explain this");
		
		
		String message = "no such thing";
		
		DocDoesNotExist ddne = new DocDoesNotExist();
		DocDoesNotExist ddne_copy = new DocDoesNotExist();
		DocDoesNotExist ddne_alt = new DocDoesNotExist();
		
		ddne.setDocId("123");
		ddne_copy.setDocId("123");
		ddne_alt.setDocId("123456789");
		
		DDNEex = new DocDoesNotExist_Exception(message, ddne);
		DDNEex_COPY = new DocDoesNotExist_Exception(message, ddne_copy);
		DDNEex_ALT = new DocDoesNotExist_Exception(message, ddne_alt);
		
		
		message = "no such user";
		
		UserDoesNotExist uddne = new UserDoesNotExist();
		UserDoesNotExist uddne_copy = new UserDoesNotExist();
		UserDoesNotExist uddne_alt = new UserDoesNotExist();
		
		uddne.setUserId("Manuel");
		uddne_copy.setUserId("Manuel");
		uddne_alt.setUserId("Joaquim");
		
		UDNEex = new UserDoesNotExist_Exception(message, uddne);
		UDNEex_COPY = new UserDoesNotExist_Exception(message, uddne_copy);
		UDNEex_ALT = new UserDoesNotExist_Exception(message, uddne_alt);
	}
	
	
	@Test
	public void content() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception{
		Response r1 = new Response(CONTENT,1);
		Response r2 = new Response(CONTENT_COPY,2);
		Response r3 = new Response(CONTENT_ALT,3);
		
		
		assertEquals(CONTENT, r1.getContent());
		assertEquals(CONTENT_COPY, r2.getContent());
		assertEquals(CONTENT_ALT, r3.getContent());
	}
	
	
	@Test (expected=Exception.class)
	public void throwIAVException() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception{
		Response r = new Response(IAVex,1);
		
		r.getContent();
	}
	
	
	@Test (expected=UserDoesNotExist_Exception.class)
	public void throwUDNEException() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception{
		Response r = new Response(UDNEex,1);
		
		r.getContent();
	}
	
	
	@Test (expected=DocDoesNotExist_Exception.class)
	public void throwDDNEException() throws InvalidAttributeValueException, UserDoesNotExist_Exception, DocDoesNotExist_Exception, CapacityExceeded_Exception{
		Response r = new Response(DDNEex,1);
		
		r.getContent();
	}
	
	
	@Test
	public void contentEquals(){
		Response r1 = new Response(CONTENT,1);
		Response r2 = new Response(CONTENT_COPY,2);
		
		assertTrue(r1.equals(r2));
	}
	
	
	@Test
	public void contentDiffers(){
		Response r1 = new Response(CONTENT,1);
		Response r2 = new Response(CONTENT_ALT,2);
		
		assertFalse(r1.equals(r2));
	}
	
	
	/*
	@Test
	public void differentTypesOfResponsesDiffer(){
		ArrayList<Response> rlist = new ArrayList<Response>();
		
		Response r1 = new Response(CONTENT, 1); 
		rlist.add(r1);
		Response r2 = new Response(GENERIC_EXCEPTION,2); 
		rlist.add(r2);
		Response r3 = new Response(IAVex,3); 
		rlist.add(r3);
		Response r4 = new Response(DDNEex,4); 
		rlist.add(r4);
		Response r5 = new Response(UDNEex,5); 
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
	public void genericExceptionEquals(){
		Response r1 = new Response(GENERIC_EXCEPTION,1);
		Response r2 = new Response(GENERIC_EXCEPTION_COPY,2);
		
		assertTrue(r1.equals(r2));
	}
	
	
	@Test
	public void genericExceptionDiffers(){
		Response r1 = new Response(GENERIC_EXCEPTION,1);
		Response r2 = new Response(GENERIC_EXCEPTION_ALT,2);
		
		assertFalse(r1.equals(r2));
	}
	
	
	@Test
	public void iavexEquals(){
		Response r1 = new Response(IAVex,1);
		Response r2 = new Response(IAVex_COPY,2);
		
		assertTrue(r1.equals(r2));
	}
	
	
	@Test
	public void iavExDiffers(){
		Response r1 = new Response(IAVex,1);
		Response r2 = new Response(IAVex_ALT,2);
		
		assertFalse(r1.equals(r2));
	}
	
	
	@Test
	public void ddneExEquals(){
		Response r1 = new Response(DDNEex,1);
		Response r2 = new Response(DDNEex_COPY,2);
		
		assertTrue(r1.equals(r2));
	}
	
	
	@Test
	public void ddneExDiffers(){
		Response r1 = new Response(DDNEex,1);
		Response r2 = new Response(DDNEex_ALT,2);
		
		assertFalse(r1.equals(r2));
	}
	
	
	@Test
	public void udneExEquals(){
		Response r1 = new Response(UDNEex,1);
		Response r2 = new Response(UDNEex_COPY,2);
		
		assertTrue(r1.equals(r2));
	}
	
	
	@Test
	public void udneExDiffers(){
		Response r1 = new Response(UDNEex,1);
		Response r2 = new Response(UDNEex_ALT,2);
		
		assertFalse(r1.equals(r2));
	}
	*/
}
