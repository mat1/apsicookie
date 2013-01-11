package ch.fhnw.apsi.cookies.server;

import java.io.IOException;

import ch.fhnw.apsi.cookies.server.validation.RequestValidator;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class SecureContentHandler implements HttpHandler {

	private static final String THE_ANSWER = "42\n";
	private static final String ERROR = "You are not allowed to see this.";
	private RequestValidator validator;
	
	private SecureContentHandler(RequestValidator val) {
		this.validator = val;
	}
	
	@Override
	public void handle(HttpExchange exch) throws IOException {
		if(validator.isValid(exch.getRequestHeaders())) {
			HttpHelper.writeResponse(validator.getSessionManager().getCookieData(HttpHelper.getToken(exch)), THE_ANSWER, exch, validator.getSessionManager());
		} else {
			HttpHelper.writeError(500, ERROR, exch);
		}
		
	}

	public static SecureContentHandler createSecureContentHandler(RequestValidator val) {
		return new SecureContentHandler(val);
	}
}
