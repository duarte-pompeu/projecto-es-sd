package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.tecnico.bubbledocs.Cache;
import pt.tecnico.bubbledocs.domain.BubbleDocs;

public class CacheTest extends BubbleDocsServiceTest {
	Cache cache;
	String ZAC_UN = "zacarias";
	String ZAC_PASS = "abcdef";
	
	String JUBI_UN = "jubileu";
	String JUBI_PASS = "abcdef";
	
	
	@Override
	public void populate4Test() {
		cache = BubbleDocs.getInstance().getCache();
		cache.put(ZAC_UN, ZAC_PASS);
		cache.put(JUBI_UN, JUBI_PASS);
	}
	
	
	@Test
	public void populateSuccess(){
		assertTrue(cache.validate(ZAC_UN, ZAC_PASS));
		assertTrue(cache.validate(JUBI_UN, JUBI_PASS));
	}
	
	
	@Test
	public void noUser(){
		String bad_user = "Hello I'm root. Let me in.";
		assertFalse(cache.hasUser(bad_user));
		assertFalse(cache.validate(bad_user, "any pass"));
	}
	
	
	@Test
	public void badPass(){
		String bad_pass = "This is a bad password. A very very bad password.";
		assertTrue(cache.validate(JUBI_UN, JUBI_PASS));
		assertFalse(cache.validate(JUBI_UN, ""));
		assertFalse(cache.validate(JUBI_UN, bad_pass));
	}
	
	
	@Test
	public void cleanCache(){
		assertTrue(cache.hasUser(JUBI_UN));
		assertTrue(cache.hasUser(ZAC_UN));
		
		cache.removeFromCache(JUBI_UN);
		cache.removeFromCache(ZAC_UN);
		
		assertFalse(cache.hasUser(JUBI_UN));
		assertFalse(cache.hasUser(ZAC_UN));
	}
}
