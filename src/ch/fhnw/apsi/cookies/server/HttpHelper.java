package ch.fhnw.apsi.cookies.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import ch.fhnw.apsi.cookies.server.cookies.SessionManager;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class HttpHelper {
	public static void writeError(int status, String message, HttpExchange exchange) {
		try {
			exchange.sendResponseHeaders(status, message.length());
			
			OutputStream os = exchange.getResponseBody();
			os.write(message.getBytes());
			os.close();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public static void writeResponse(String token, String message, HttpExchange exchange, SessionManager mgr) { 
		try {
			String cookie = mgr.getSessionCookie(token, "HEADERINFO");
			if(cookie != null) {
				System.out.println("Writing null cookie.");
				Headers headerResponse = exchange.getResponseHeaders();
				headerResponse.set("Set-Cookie", "token="+cookie+";" +
												 "Max-Age=60; Version=\"1\"");
			}
			exchange.sendResponseHeaders(200, message.length());
			
			OutputStream os = exchange.getResponseBody();
			os.write(message.getBytes());
			os.close();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public static String getToken(HttpExchange exchange) {
		return getTokenCookieValue(exchange.getRequestHeaders());
	}
	
	public static String getTokenCookieValue(Headers headers) {
		System.out.println(headers.get("Cookie"));
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
