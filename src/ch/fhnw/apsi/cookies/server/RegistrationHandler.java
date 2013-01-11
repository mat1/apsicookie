package ch.fhnw.apsi.cookies.server;

import java.io.IOException;

import ch.fhnw.apsi.cookies.server.cookies.SessionManager;
import ch.fhnw.apsi.cookies.server.validation.UserValidator;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RegistrationHandler implements HttpHandler {

	private final SessionManager sessionMgr;
	private final UserValidator userValidator;
	
	private RegistrationHandler(SessionManager sessionMgr, UserValidator userValidator) {
		this.userValidator = userValidator;
		this.sessionMgr = sessionMgr;
	}
	
	@Override
	public void handle(HttpExchange exch) throws IOException {
		
	}

	public static RegistrationHandler createRegistrationHandler(SessionManager sessionMgr, UserValidator userValidator) {
		return new RegistrationHandler(sessionMgr, userValidator);
	}

}
