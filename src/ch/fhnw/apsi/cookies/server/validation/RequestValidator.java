package ch.fhnw.apsi.cookies.server.validation;

import java.net.InetAddress;

import ch.fhnw.apsi.cookies.server.HttpHelper;
import ch.fhnw.apsi.cookies.server.cookies.SessionManager;

import com.sun.net.httpserver.Headers;

public final class RequestValidator {
	private final HeaderInfoHasher hasher;
	private final SessionManager mgr;
	
	private RequestValidator(SessionManager mgr, HeaderInfoHasher hsh) {
		this.mgr = mgr;
		this.hasher = hsh;
		hasher.getClass();
	}
	
	public boolean isValid(InetAddress address, Headers headers) {
		final String cookie = HttpHelper.getTokenCookieValue(headers);
		
		if(!mgr.isValid(cookie)) return false;
		
		final String expected = mgr.getCookieData(cookie);
		final String actual = hasher.generateHeaderInfoHash(address, headers);
		
		System.out.println("COOKIE-DATA: " + expected);
		System.out.println("ACTUAL-REQU: " + actual);
		
		return expected.equals(actual);
	}
	
	public SessionManager getSessionManager() {
		return mgr;
	}
	
	public static RequestValidator create(SessionManager mgr, HeaderInfoHasher hsh) {
		return new RequestValidator(mgr, hsh);
	}
}
