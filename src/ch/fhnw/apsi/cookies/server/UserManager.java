package ch.fhnw.apsi.cookies.server;

import java.util.HashMap;
import java.util.Map;

import ch.fhnw.apsi.cookies.server.model.User;
import ch.fhnw.apsi.cookies.server.validation.UserValidator;

public class UserManager {
	private final UserValidator validator = UserValidator.createDefaultUserValidator(this);
	private final Map<String, User> users = new HashMap<>();
	
	public User createUser(String name, String email) {
		User user = User.createUser(name, email);
		
		if(validator.isValid(user)) {
			users.put(name, user);
		}
		
		return user;
	}
	
	public User getUser(String name) {
		return users.get(name);
	}
}
