package ch.fhnw.apsi.cookies.server.validation;

import ch.fhnw.apsi.cookies.server.UserManager;
import ch.fhnw.apsi.cookies.server.model.User;

public class UserValidator {
	private final UserManager mgr;
	
	private UserValidator(UserManager mgr) { 
		this.mgr = mgr;
	}
	
	/**
	 * Checks if a given user is valid and may proceed registration.
	 * 
	 * Check for valid username/email/password.
	 * 
	 * @param u the user to check
	 * @return whether the check was succesful or not
	 */
	public boolean isValid(User u) {
		return false;
	}
	
	public UserValidator createDefaultUserValidator(UserManager mgr) {
		return new UserValidator(mgr);
	}
	
	public static class InvalidEmailException extends RuntimeException {
		public InvalidEmailException(String msg) {
			super("Invalid email address provided: " + msg);
		}
	}
	
	public static class UserAlreadyExistingException extends RuntimeException {
		public UserAlreadyExistingException(String name) {
			super("User with name " + name + " already exists.");
		}
	}
}
