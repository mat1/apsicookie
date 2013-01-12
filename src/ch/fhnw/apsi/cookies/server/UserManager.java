package ch.fhnw.apsi.cookies.server;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import ch.fhnw.apsi.cookies.server.model.User;
import ch.fhnw.apsi.cookies.server.validation.UserValidator;

public final class UserManager {
	private final UserValidator validator = UserValidator.createDefaultUserValidator(this);
	private final Map<String, User> users = new HashMap<>();
	
	public UserManager() {}
	
	@CheckReturnValue
	public User createUser(String name, String email) {
		User user = User.createUser(name, email);
		
		if(validator.isValid(user)) {
			users.put(name, user);
		}
		
		return user;
	}
	
	@CheckReturnValue
	public User getUser(@Nonnull String name) {
		return users.get(name);
	}
}
