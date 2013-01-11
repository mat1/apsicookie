package ch.fhnw.apsi.cookies.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.fhnw.apsi.cookies.server.cookies.SessionManager;
import ch.fhnw.apsi.cookies.server.model.User;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RegistrationHandler implements HttpHandler {
	
	private static final String SUCCESS_HTML = "res/success.html";
	private final String content;
	
	private final SessionManager sessionManager;
	private final UserManager userManager;
	
	private RegistrationHandler(SessionManager sessionMgr, UserManager userManager) throws FileNotFoundException {
		this.userManager = userManager;
		this.sessionManager = sessionMgr;
		this.content = FileHelper.fileToString(new File(SUCCESS_HTML));
	}
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		InputStream is = exchange.getRequestBody();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line = reader.readLine();
		
		/* Read username and mail */
		String[] values = line.split("&");
		String username = keyValueToValue(values[0]);
		String mail = keyValueToValue(values[1]);
		
		try {
			User u = userManager.createUser(username, mail);
		
			String token = sessionManager.createSession(u);
			HttpHelper.writeResponse(token, content, exchange, sessionManager);
		} catch (Exception ex) {
			HttpHelper.writeError(500, ex.getMessage(), exchange);
		} 
	}
	
	private String keyValueToValue(String keyValue){
		String[] pair = keyValue.split("=");
		return pair[1];
	}
	
	public static RegistrationHandler createRegistrationHandler(SessionManager sessionMgr, UserManager userManager) throws FileNotFoundException {
		return new RegistrationHandler(sessionMgr, userManager);
	}

}
