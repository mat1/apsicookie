package ch.fhnw.apsi.cookies.server.cookies;

import ch.fhnw.apsi.cookies.server.model.ClientSession;
import ch.fhnw.apsi.cookies.server.model.User;

public class SessionManager {
	public String createSession(User user) {
		return "";
	}
	
	public ClientSession getSessionFromToken(String token) {
		return null;
	}
	
	public void removeSession(String token) {
		
	}
	
	private boolean hadTimeout(ClientSession sess) {
		return false;
	}
}
