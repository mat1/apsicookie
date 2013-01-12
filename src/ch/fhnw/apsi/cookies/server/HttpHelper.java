package ch.fhnw.apsi.cookies.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.fhnw.apsi.cookies.server.cookies.SessionManager;
import ch.fhnw.apsi.cookies.server.validation.HeaderInfoHasher;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public final class HttpHelper {
	
	private static final Logger logger = LogManager.getLogger(HttpHelper.class.getName());
	
	private static final HeaderInfoHasher hasher = HeaderInfoHasher.create();
	
	private HttpHelper() {}
	
	public static void writeError(int status, String message,@Nonnull HttpExchange exchange) {
		try {
			Headers headerResponse = exchange.getResponseHeaders();
			headerResponse.set("Set-Cookie", "token=nothing;" +
											 "Max-Age=0; Version=\"1\"");
			exchange.sendResponseHeaders(status, message.length());
			
			OutputStream os = exchange.getResponseBody();
			os.write(message.getBytes());
			os.close();
		} catch (IOException ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		}
	}
	
	public static void writeResponse(String token, String message,@Nonnull HttpExchange exchange, SessionManager mgr) { 
		final String data = hasher.generateHeaderInfoHash(exchange.getRemoteAddress().getAddress(), exchange.getRequestHeaders());
		
		try {
			String cookie = mgr.getSessionCookie(token, data);
			if(cookie != null) {
				Headers headerResponse = exchange.getResponseHeaders();
				headerResponse.set("Set-Cookie", "token="+cookie+";" +
												 "Max-Age=60; Version=\"1\"");
			} 
			exchange.sendResponseHeaders(200, message.length());
			
			OutputStream os = exchange.getResponseBody();
			os.write(message.getBytes());
			os.close();
		} catch (IOException ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		}
	}
	
	public static String getTokenFromCookie(String cookie) {
		final String[] splitted = cookie.split("-");
		if(splitted.length >= 1) {
			return splitted[0];
		}
		return "";
	}
	
	public static String getTokenCookie(HttpExchange exchange) {
		return getTokenCookieValue(exchange.getRequestHeaders());
	}
	
	public static String getTokenCookieValue(Headers headers) {
		logger.info("Received cookie: {}", headers.get("Cookie"));
		List<String> str = (List<String>) headers.get("Cookie");
		if(str == null || str.size() != 1) return "";
		
		for(String cookie : str.get(0).split(";")) {
			String cookieName = cookie.substring(0, cookie.indexOf("="));
			if("token".equals(cookieName.trim())) {
				return cookie.substring(cookie.indexOf("=") + 1, cookie.length());
			}
		}
		return "";
	}
}
