package ch.fhnw.apsi.cookies.server.validation;

import java.util.Hashtable;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

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
		if(!emailFormatValid(u.getMail()))
			throw new InvalidEmailException("Invalid email format");
		
		if(!mxRecordExists(u.getMail()))
			throw new InvalidEmailException("Invalid domain provided");
		
		if(userExists(u.getUserName()))
			throw new UserAlreadyExistingException(u.getUserName());
		
		return true;
	}
	
	/**
	 * Taken from http://www.linuxjournal.com/article/9585.
	 * 
	 * @param mail the email address.
	 * @return true if the email format is valid
	 */
	private boolean emailFormatValid(String mail) {
		return true;
	}
	
	private boolean mxRecordExists(String mail) {
		try {
			String[] split = mail.split("@");
			if(split.length != 2) return false;
			
			Hashtable env = new Hashtable();
		    env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
		    DirContext ictx = new InitialDirContext( env );
		    Attributes attrs = ictx.getAttributes(split[1], new String[] { "MX" });
		    Attribute attr = attrs.get( "MX" );
		    if( attr == null ) return false;
		    
		    return true;
		} catch (NamingException ex) {
			return false;
		}
	}
	
	private boolean userExists(String name) {
		return mgr.getUser(name) != null;
	}
	
	public static UserValidator createDefaultUserValidator(UserManager mgr) {
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
