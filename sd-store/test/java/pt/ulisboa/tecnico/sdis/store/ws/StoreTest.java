package pt.ulisboa.tecnico.sdis.store.ws;

import org.junit.After;
import org.junit.Before;

public class StoreTest {
	
	@Before
	public void setUp(){
		populate4Test();
	}
	
	@After
	public void tearDown(){
		destroy();
	}
	
	
	public void populate4Test(){
		
	}
	
	public void destroy(){
		
	}
	
	//methods useful for testing
	public void reset(Storage storage){
		storage.init();
	}
}
