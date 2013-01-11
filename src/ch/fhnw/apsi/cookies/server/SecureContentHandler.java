package ch.fhnw.apsi.cookies.server;

import java.io.IOException;
import java.io.OutputStream;

import ch.fhnw.apsi.cookies.server.validation.RequestValidator;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class SecureContentHandler implements HttpHandler {

	private static final String THE_ANSWER = "42\n";
	private RequestValidator validator;
	
	private SecureContentHandler(RequestValidator val) {
		this.validator = val;
	}
	
	@Override
	public void handle(HttpExchange exch) throws IOException {
		exch.getRequestBody().close();
		exch.sendResponseHeaders(200, THE_ANSWER.length());
        OutputStream os = exch.getResponseBody();
        os.write(THE_ANSWER.getBytes());
        os.close();
	}

	public static SecureContentHandler createSecureContentHandler(RequestValidator val) {
		return new SecureContentHandler(val);
	}
}
