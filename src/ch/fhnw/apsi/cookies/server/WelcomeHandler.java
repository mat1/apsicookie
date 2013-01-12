package ch.fhnw.apsi.cookies.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.Nonnull;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public final class WelcomeHandler implements HttpHandler {

	private static final String INDEX_HTML = "res/index.html";
	private final String content;

	public WelcomeHandler() throws FileNotFoundException {
		content = FileHelper.fileToString(new File(INDEX_HTML));
	}

	@Override
	public void handle(@Nonnull HttpExchange exchange) throws IOException {
		exchange.sendResponseHeaders(200, content.length());
		OutputStream os = exchange.getResponseBody();
		os.write(content.getBytes());
		os.close();
	}

}
