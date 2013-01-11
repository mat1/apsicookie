package ch.fhnw.apsi.cookies.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class WelcomeHandler implements HttpHandler {

	private static final String INDEX_HTML = "res/index.html";
	private final String content;

	public WelcomeHandler() throws FileNotFoundException {
		content = FileHelper.fileToString(new File(INDEX_HTML));
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		exchange.sendResponseHeaders(200, content.length());
		OutputStream os = exchange.getResponseBody();
		os.write(content.getBytes());
		os.close();
	}

}
