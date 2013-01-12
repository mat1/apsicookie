package ch.fhnw.apsi.cookies.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public final class WelcomeHandler implements HttpHandler {

	private static final Logger logger = LogManager.getLogger(WelcomeHandler.class.getName());
	
	private static final String INDEX_HTML = "res/index.html";
	private final String content;

	private WelcomeHandler() throws FileNotFoundException {
		content = FileHelper.fileToString(new File(INDEX_HTML));
	}

	@Override
	public void handle(@Nonnull HttpExchange exchange) throws IOException {
		logger.info("Handle welcome page request.");
		exchange.sendResponseHeaders(200, content.length());
		OutputStream os = exchange.getResponseBody();
		os.write(content.getBytes());
		os.close();
	}
	
	public static WelcomeHandler create() throws FileNotFoundException {
		return new WelcomeHandler();
	}

}
