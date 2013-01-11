package ch.fhnw.apsi.cookies.server.validation;

import ch.fhnw.apsi.cookies.server.cookies.SessionManager;

import com.sun.net.httpserver.Headers;

public class RequestValidator {
	private final HeaderInfoHasher hasher;
	private final SessionManager mgr;
	
	private RequestValidator(SessionManager mgr, HeaderInfoHasher hsh) {
		this.mgr = mgr;
		this.hasher = hsh;
	}
	
	public boolean isValid(Headers headers) {
		return true;
	}
	
	public static RequestValidator createRequestValidator(SessionManager mgr, HeaderInfoHasher hsh) {
		return new RequestValidator(mgr, hsh);
	}
}
