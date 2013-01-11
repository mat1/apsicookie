package ch.fhnw.apsi.cookies.server;

import java.io.IOException;

import ch.fhnw.apsi.cookies.server.validation.RequestValidator;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class SecureContentHandler implements HttpHandler {

	private RequestValidator validator;
	
	private SecureContentHandler(RequestValidator val) {
		this.validator = val;
	}
	
	@Override
	public void handle(HttpExchange exch) throws IOException {
		
	}

	public static SecureContentHandler createSecureContentHandler(RequestValidator val) {
		return new SecureContentHandler(val);
	}
}
