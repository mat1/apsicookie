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

public final class CssHandler implements HttpHandler {

	private static final Logger logger = LogManager.getLogger(CssHandler.class.getName());
	
	private static final String INDEX_HTML = "res/bootstrap.min.css";
	private final String content;

	private CssHandler() throws FileNotFoundException {
		content = FileHelper.fileToString(new File(INDEX_HTML));
	}

	@Override
	public void handle(@Nonnull HttpExchange exchange) throws IOException {
		logger.info("Handle css request.");
		exchange.sendResponseHeaders(200, content.length());
		OutputStream os = exchange.getResponseBody();
		os.write(content.getBytes());
		os.close();
	}
	
	public static CssHandler create() throws FileNotFoundException {
		return new CssHandler();
	}

}
