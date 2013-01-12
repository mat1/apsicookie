package ch.fhnw.apsi.cookies.server.validation;

import java.net.InetAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.fhnw.apsi.cookies.server.HttpHelper;
import ch.fhnw.apsi.cookies.server.cookies.SessionManager;

import com.sun.net.httpserver.Headers;

public final class RequestValidator {
	private static final Logger logger = LogManager.getLogger(RequestValidator.class.getName());
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
		
		logger.info("Cookie request: {}", expected);
		logger.info("Actual request: {}", actual);
		
		return expected.equals(actual);
	}
	
	public SessionManager getSessionManager() {
		return mgr;
	}
	
	public static RequestValidator create(SessionManager mgr, HeaderInfoHasher hsh) {
		return new RequestValidator(mgr, hsh);
	}
}
