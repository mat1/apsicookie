package ch.fhnw.apsi.cookies.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.fhnw.apsi.cookies.server.cookies.SessionManager;
import ch.fhnw.apsi.cookies.server.validation.HeaderInfoHasher;
import ch.fhnw.apsi.cookies.server.validation.RequestValidator;

import com.sun.net.httpserver.HttpServer;

public final class Server {
	private static final Logger logger = LogManager.getLogger(Server.class.getName());
	private static final int MAX_QUEUE = 0; /* use system default */
	private static final int PORT = 8000;
	
	private final UserManager userManager;
	private final SessionManager sessionManager;
	
	private Server() {
		userManager = UserManager.create();
		sessionManager = SessionManager.create();
	}
	
	public void serve() {
		try {
			logger.info("Start http server...");
			HttpServer server = HttpServer.create(new InetSocketAddress(PORT), MAX_QUEUE);
			server.createContext("/", WelcomeHandler.create());
			server.createContext("/register", RegistrationHandler.create(sessionManager, userManager));
			server.createContext("/secured", SecureContentHandler.create(RequestValidator.create(sessionManager, HeaderInfoHasher.create())));
			server.setExecutor(null); // creates a default executor
			server.start();
			logger.info("Http server successfully started.");
		} catch (IOException ex) {
			logger.fatal("Error starting server.", ex);
			throw new RuntimeException(ex);
		}
	}
	
	public static void main(String args[]) {
		new Server().serve();
	}
}
