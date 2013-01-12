package ch.fhnw.apsi.cookies.server;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.fhnw.apsi.cookies.server.validation.RequestValidator;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public final class SecureContentHandler implements HttpHandler {
	
	private static final Logger logger = LogManager.getLogger(SecureContentHandler.class.getName());
	
	private static final String THE_ANSWER = "42\n";
	private static final String ERROR = "You are not allowed to see this.";
	private RequestValidator validator;
	
	private SecureContentHandler(RequestValidator val) {
		this.validator = val;
	}
	
	@Override
	public void handle(@Nonnull HttpExchange exch) throws IOException {
		logger.info("Handle secure contet request.");
		
		if(validator.isValid(exch.getLocalAddress().getAddress(), exch.getRequestHeaders())) {
			logger.info("Show secure content.");
			final String requestToken = HttpHelper.getTokenFromCookie(HttpHelper.getTokenCookie(exch));
			final String token = validator.getSessionManager().updateSession(requestToken);
			HttpHelper.writeResponse(token, THE_ANSWER, exch, validator.getSessionManager());
		} else {
			logger.warn("In valid request from: " + exch.getLocalAddress().getAddress());
			HttpHelper.writeError(500, ERROR, exch);
		}
		
	}

	public static SecureContentHandler create(RequestValidator val) {
		return new SecureContentHandler(val);
	}
}
