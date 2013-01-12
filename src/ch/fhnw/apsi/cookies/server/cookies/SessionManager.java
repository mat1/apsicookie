package ch.fhnw.apsi.cookies.server.cookies;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import ch.fhnw.apsi.cookies.server.model.ClientSession;
import ch.fhnw.apsi.cookies.server.model.User;

public final class SessionManager {
	private static final long SESSION_TIMEOUT = 60 * 1000;
	private static final int TOKEN_BITS = 130;
	private static final int SERVER_KEY_SIZE = 256;
	
	private final SecureRandom random = new SecureRandom();
	private final Map<String,ClientSession> sessions = new HashMap<>();
	private final CookieManager cookieManager = CookieManager.create();
	
	private SessionManager() {}
	
	@CheckReturnValue
	public String createSession(@Nonnull User user) {
		final String token = generateToken();
		final ClientSession sess = ClientSession.createSession(user, token, getNextExpiringTimeFromNow(), cookieManager.generateKey(SERVER_KEY_SIZE));
		sessions.put(token, sess);
		
		return token;
	}
	
	@CheckReturnValue
	public String getSessionCookie(String token, String data) {
		ClientSession session = getSessionFromToken(token);
		if(session == null) return null;
		
		return cookieManager.getCookieString(token, session.getExpiringTime(), data, session.getCurrentKey());
	}
	
	@CheckReturnValue
	public boolean isValid(String cookie) {
		final String token = cookieManager.extractToken(cookie);
		if(token == null) return false;
		
		final ClientSession sess = getSessionFromToken(token);
		if(sess == null) return false;
		
		return cookieManager.isValid(sess.getCurrentKey(), cookie);
	}
	
	@CheckReturnValue
	public String getCookieData(String cookie) {
		final String token = cookieManager.extractToken(cookie);
		if(token == null) return null;
		
		final ClientSession sess = getSessionFromToken(token);
		if(sess == null) return null;
		
		return cookieManager.getCookieData(sess.getCurrentKey(), cookie);
	}
	
	@CheckReturnValue
	protected ClientSession getSessionFromToken(String token) {
		return sessions.get(token);
	}
	
	public void removeSession(String token) {
		sessions.remove(token);
	}
	
	@CheckReturnValue
	public String updateSession(String oldToken) {
		final ClientSession sess = sessions.get(oldToken);
		if(sess == null) return null;
		
		removeSession(oldToken);
		
		final String newToken = generateToken();
		sess.setExpiringTime(getNextExpiringTimeFromNow());
		sess.setCurrentKey(cookieManager.generateKey(SERVER_KEY_SIZE));
		sess.setToken(newToken);
		
		sessions.put(newToken, sess);
		return newToken;
	}
	
	@CheckReturnValue
	private long getNextExpiringTimeFromNow() {
		return System.currentTimeMillis() + SESSION_TIMEOUT;
	}
	
	@CheckReturnValue
	private String generateToken() {
		return new BigInteger(TOKEN_BITS, random).toString(32);
	}
	
	public static SessionManager create() {
		return new SessionManager();
	}
}
