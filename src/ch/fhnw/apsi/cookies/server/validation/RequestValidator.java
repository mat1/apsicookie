package ch.fhnw.apsi.cookies.server.validation;

import java.util.List;

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
		String cookie = getTokenCookieValue(headers);
		
		if(!mgr.isValid(cookie)) return false;
		System.out.println("COOKIE-DATA: " + mgr.getCookieData(cookie));
		return true;
	}
	
	public String getTokenCookieValue(Headers headers) {
		System.out.println(headers.get("Cookie"));
		List<String> str = (List<String>) headers.get("Cookie");
		if(str == null || str.size() != 1) return "";
		
		for(String cookie : str.get(0).split(";")) {
			String cookieName = cookie.substring(0, cookie.indexOf("="));
			if("token".equals(cookieName.trim())) {
				return cookie.substring(cookie.indexOf("=") + 1, cookie.length());
			}
		}
		return "";
	}
	
	public static RequestValidator createRequestValidator(SessionManager mgr, HeaderInfoHasher hsh) {
		return new RequestValidator(mgr, hsh);
	}
}
