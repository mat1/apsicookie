package ch.fhnw.apsi.cookies.server.cookies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CookieManagerTest {

	private CookieManager mgr;
	
	@Before
	public void setUp() {
		mgr = new CookieManager();
	}
	
	@Test
	public void testEncryptDecrypt() {
		String msg = "Hello Cookie!";
		
		String enc = mgr.encrypt("blobba".getBytes(), msg);
		String dec = mgr.decrypt("blobba".getBytes(), enc);
		
		assertEquals(msg, dec);
	}
	
	@Test
	public void testHMAC() {
		String msg = "Hello Cookie!";
		
		assertFalse(msg.equals(mgr.hmacEncode("ultraSecretServerKey".getBytes(), msg)));
	}
	
	@Test
	public void testHMACDiffrence() throws Exception {
		String msg = "Hello Cookie!";
		String msg2 = "Hello Cookie2!";
		
		String a = new String(mgr.hmacEncode("asdf".getBytes(), msg));
		String b = new String(mgr.hmacEncode("asdf".getBytes(), msg2));

		assertFalse(a.equals(b));
		assertFalse(b.equals(a));
	}
	
	@Test
	public void testCookieBuild() throws Exception {
		byte[] key = mgr.generateKey(20);
		String msg = "Hello Cookie!";

		String cookie = mgr.getCookieString("BLOBB", Long.MAX_VALUE, msg, key);
		
		assertTrue(mgr.isValid(key, cookie));
		assertEquals(msg, mgr.getCookieData(key, cookie));
	}
	
	@Test
	public void testIllegalCookie() throws Exception {
		byte[] key = mgr.generateKey(20);
		String msg = "Hello Cookie!";

		String cookie = mgr.getCookieString("BLOBB", Long.MAX_VALUE, msg, key);
		cookie = "A" + cookie; 
		
		assertFalse(mgr.isValid(key, cookie));
	}
	
	@Test
	public void testIllegalCookieTime() throws Exception {
		byte[] key = mgr.generateKey(20);
		String msg = "Hello Cookie!";

		String cookie = mgr.getCookieString("BLOBB", System.currentTimeMillis(), msg, key);
		
		assertFalse(mgr.isValid(key, cookie));
	}
	
	@Test
	public void testKeyGeneration() {
		CookieManager mgr = new CookieManager();
		byte[] key1 = mgr.generateKey(8);
		byte[] key2 = mgr.generateKey(8);
		
		assertTrue(key1.length == key2.length);
		
		boolean allEquals = true;
		for(int i = 0; i < key1.length && i < key2.length; i++) {
			if(key1[i] != key2[i]) {
				allEquals = false;
				break;
			}
		}
		
		assertFalse(allEquals);
	}

}
