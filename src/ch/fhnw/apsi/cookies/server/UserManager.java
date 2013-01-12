package ch.fhnw.apsi.cookies.server;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.fhnw.apsi.cookies.server.model.User;
import ch.fhnw.apsi.cookies.server.validation.UserValidator;

public final class UserManager {
	private static final Logger logger = LogManager.getLogger(UserManager.class.getName());
	private final UserValidator validator = UserValidator.createDefaultUserValidator(this);
	private final Map<String, User> users = new HashMap<>();
	
	private UserManager() {}
	
	@CheckReturnValue
	public User createUser(String name, String email) {
		User user = User.createUser(name, email);
		
		if(validator.isValid(user)) {
			logger.info("Creating new user {}", name);
			users.put(name, user);
		}
		
		return user;
	}
	
	@CheckReturnValue
	public User getUser(@Nonnull String name) {
		return users.get(name);
	}
	
	public static UserManager create() {
		return new UserManager();
	}
}
