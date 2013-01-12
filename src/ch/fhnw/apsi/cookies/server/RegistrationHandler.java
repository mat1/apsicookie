package ch.fhnw.apsi.cookies.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.fhnw.apsi.cookies.server.cookies.SessionManager;
import ch.fhnw.apsi.cookies.server.model.User;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public final class RegistrationHandler implements HttpHandler {
	
	private static final Logger logger = LogManager.getLogger(RegistrationHandler.class.getName());
	
	private static final String SUCCESS_HTML = "res/success.html";
	private final String content;

	private final SessionManager sessionManager;
	private final UserManager userManager;

	private RegistrationHandler(@Nonnull SessionManager sessionMgr,
								@Nonnull UserManager userManager) throws FileNotFoundException {
		this.userManager = userManager;
		this.sessionManager = sessionMgr;
		this.content = FileHelper.fileToString(new File(SUCCESS_HTML));
	}

	@Override
	public void handle(@Nonnull HttpExchange exchange) throws IOException {
		logger.info("Handle registraion request.");
		
		InputStream is = exchange.getRequestBody();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line = reader.readLine();

		/* Read username and mail */
		if (line == null) HttpHelper.writeError(500, "Please enter your information", exchange);
		String[] values = line.split("&");

		if (values.length != 2) HttpHelper.writeError(500, "Please enter your information", exchange);
		String username = keyValueToValue(values[0]);
		String mail = keyValueToValue(values[1]);

		try {
			User u = userManager.createUser(username, URLDecoder.decode(mail, "UTF-8"));

			String token = sessionManager.createSession(u);
			HttpHelper.writeResponse(token, content, exchange, sessionManager);
		} catch (Exception ex) {
			logger.info(ex.getMessage());
			HttpHelper.writeError(500, ex.getMessage(), exchange);
		}
	}

	private String keyValueToValue(@Nonnull String keyValue) {
		String[] pair = keyValue.split("=");
		if (pair.length != 2)
			return "";
		return pair[1];
	}

	public static RegistrationHandler create(SessionManager sessionMgr, UserManager userManager) throws FileNotFoundException {
		return new RegistrationHandler(sessionMgr, userManager);
	}

}
