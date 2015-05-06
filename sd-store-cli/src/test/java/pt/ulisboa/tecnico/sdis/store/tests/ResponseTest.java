package pt.ulisboa.tecnico.sdis.store.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.cli.Response;
import pt.ulisboa.tecnico.sdis.store.cli.StoreClient;

public class ResponseTest {
	static byte[] CONTENT;
	static byte[] CONTENT_REPLICA;
	static byte[] CONTENT_ALT;
	
	static Exception GENERIC_EXCEPTION;
	static Exception GENERIC_EXCEPTION_REPLICA;
	static Exception GENERIC_EXCEPTION_ALT;
	
	@BeforeClass
	public static void init(){
		CONTENT = StoreClient.string2bytes("LOREM IPSUM");
		CONTENT_REPLICA = StoreClient.string2bytes("LOREM IPSUM");
		CONTENT_ALT = StoreClient.string2bytes("IPSUM LOREM");
		
		GENERIC_EXCEPTION = new RuntimeException("HELLO");
		GENERIC_EXCEPTION_REPLICA = new RuntimeException("HELLO");
		GENERIC_EXCEPTION_ALT = new RuntimeException("GOODBYE");
	}
	
	@Test
	public void differentTypesOfResponses(){
		ArrayList<Response> rlist = new ArrayList<Response>();
		Response r1 = new Response(CONTENT); 
		rlist.add(r1);
		
		Response r2 = new Response(GENERIC_EXCEPTION); 
		rlist.add(r2);
		
		
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
}
