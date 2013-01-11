package ch.fhnw.apsi.cookies.server.validation;

import ch.fhnw.apsi.cookies.server.HttpHelper;
import ch.fhnw.apsi.cookies.server.cookies.SessionManager;

import com.sun.net.httpserver.Headers;

public class RequestValidator {
	private final HeaderInfoHasher hasher;
	private final SessionManager mgr;
	
	private RequestValidator(SessionManager mgr, HeaderInfoHasher hsh) {
		this.mgr = mgr;
		this.hasher = hsh;
		hasher.getClass();
	}
	
	public boolean isValid(Headers headers) {
		String cookie = HttpHelper.getTokenCookieValue(headers);
		
		if(!mgr.isValid(cookie)) return false;
		System.out.println("COOKIE-DATA: " + mgr.getCookieData(cookie));
		return true;
	}
	
	public SessionManager getSessionManager() {
		return mgr;
	}
	
	public static RequestValidator createRequestValidator(SessionManager mgr, HeaderInfoHasher hsh) {
		return new RequestValidator(mgr, hsh);
	}
}
