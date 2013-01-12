package ch.fhnw.apsi.cookies.server;

import java.io.IOException;

import javax.annotation.Nonnull;

import ch.fhnw.apsi.cookies.server.validation.RequestValidator;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public final class SecureContentHandler implements HttpHandler {

	private static final String THE_ANSWER = "42\n";
	private static final String ERROR = "You are not allowed to see this.";
	private RequestValidator validator;
	
	private SecureContentHandler(RequestValidator val) {
		this.validator = val;
	}
	
	@Override
	public void handle(@Nonnull HttpExchange exch) throws IOException {
		if(validator.isValid(exch.getLocalAddress().getAddress(), exch.getRequestHeaders())) {
			final String requestToken = HttpHelper.getTokenFromCookie(HttpHelper.getTokenCookie(exch));
			final String token = validator.getSessionManager().updateSession(requestToken);
			HttpHelper.writeResponse(token, THE_ANSWER, exch, validator.getSessionManager());
		} else {
			HttpHelper.writeError(500, ERROR, exch);
		}
		
	}

	public static SecureContentHandler create(RequestValidator val) {
		return new SecureContentHandler(val);
	}
}
