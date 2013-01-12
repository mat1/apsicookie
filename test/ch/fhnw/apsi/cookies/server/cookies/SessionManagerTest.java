package ch.fhnw.apsi.cookies.server.cookies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ch.fhnw.apsi.cookies.server.model.ClientSession;
import ch.fhnw.apsi.cookies.server.model.User;

public class SessionManagerTest {

	private User u;
	private SessionManager mgr;
	
	@Before
	public void setUp() {
		u = User.createUser("blobbname", "mail@blobb.blob");
		mgr = SessionManager.create();
	}
	
	@Test
	public void testNextToken() {
		final String token = mgr.createSession(u);
		
		assertNotNull(token);
		assertNotNull(mgr.getSessionFromToken(token));
		
		ClientSession sess = mgr.getSessionFromToken(token);
		assertNotNull(sess.getCurrentKey());
		assertTrue(sess.getExpiringTime() > System.currentTimeMillis());
	}
	
	@Test
	public void testUpdateToken() {
		final String token = mgr.createSession(u);
		final byte[] oldKey = mgr.getSessionFromToken(token).getCurrentKey();
		final long oldTime = mgr.getSessionFromToken(token).getExpiringTime();
		
		assertNotNull(token);
		assertNotNull(mgr.getSessionFromToken(token));
		
		final String newToken = mgr.updateSession(token);
		
		assertNotNull(newToken);
		assertNotNull(mgr.getSessionFromToken(newToken));
		assertSame(u, mgr.getSessionFromToken(newToken).getOwner());
		assertNull(mgr.getSessionFromToken(token));
		assertFalse(oldTime == mgr.getSessionFromToken(newToken).getExpiringTime());
		assertFalse(oldKey == mgr.getSessionFromToken(newToken).getCurrentKey());
	}

	
	@Test
	public void testCookieHandling() {
		final String msg = "BLOBB";
		final String token = mgr.createSession(u);

		final String acookie = mgr.getSessionCookie(token, msg);
		assertTrue(mgr.isValid(acookie));
		assertEquals(msg, mgr.getCookieData(acookie));
		mgr.updateSession(token);
		assertFalse(mgr.isValid(acookie));
	}
}
