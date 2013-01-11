package ch.fhnw.apsi.cookies.server.cookies;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ch.fhnw.apsi.cookies.server.model.User;

public class SessionManagerTest {

	private User u;
	private SessionManager mgr;
	
	@Before
	public void setUp() {
		u = User.createUser("blobbname", "mail@blobb.blob");
		mgr = new SessionManager();
	}
	
	@Test
	public void testNextToken() {
		final String token = mgr.createSession(u);
		
		assertNotNull(token);
		assertNotNull(mgr.getSessionFromToken(token));
		
		final String newToken = mgr.updateSession(token);
		
		assertNotNull(newToken);
		assertNotNull(mgr.getSessionFromToken(newToken));
		assertSame(u, mgr.getSessionFromToken(newToken).getOwner());
		assertNull(mgr.getSessionFromToken(token));
	}

}
