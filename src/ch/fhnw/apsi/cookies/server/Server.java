package ch.fhnw.apsi.cookies.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import ch.fhnw.apsi.cookies.server.cookies.SessionManager;
import ch.fhnw.apsi.cookies.server.validation.HeaderInfoHasher;
import ch.fhnw.apsi.cookies.server.validation.RequestValidator;
import ch.fhnw.apsi.cookies.server.validation.UserValidator;

import com.sun.net.httpserver.HttpServer;

public class Server {
	private static final int maxQueue = 0; /* use system default */
	private final int port = 8080;
	
	private final UserManager userManager;
	private final SessionManager sessManager;
	
	private Server() {
		userManager = new UserManager();
		sessManager = new SessionManager();
	}
	
	public void serv() {
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(port), maxQueue);
			server.createContext("/", new WelcomeHandler());
			server.createContext("/register", RegistrationHandler.createRegistrationHandler(sessManager, UserValidator.createDefaultUserValidator(userManager)));
			server.createContext("/secured", SecureContentHandler.createSecureContentHandler(RequestValidator.createRequestValidator(sessManager, new HeaderInfoHasher())));
			server.setExecutor(null); // creates a default executor
			server.start();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
}
