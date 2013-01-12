package ch.fhnw.apsi.cookies.server.validation;

import java.util.Hashtable;
import java.util.regex.Pattern;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.fhnw.apsi.cookies.server.UserManager;
import ch.fhnw.apsi.cookies.server.model.User;

public final class UserValidator {
	private static final Logger logger = LogManager.getLogger(UserValidator.class.getName());
	private final Pattern first = Pattern.compile("^[^@]{1,64}@[^@]{1,255}$");
	private final Pattern second = Pattern.compile("^(([A-Za-z0-9!#$%&'*+/=?^_`{|}~-][A-Za-z0-9!#$%&\'*+/=?^_`{|}~\\.-]{0,63})|(\"[^(\\|\")]{0,62}\"))$");
	private final Pattern third = Pattern.compile("^(([A-Za-z0-9][A-Za-z0-9-]{0,61}[A-Za-z0-9])|([A-Za-z0-9]+))$");
	
	private final UserManager manager;
	
	private UserValidator(@Nonnull UserManager mgr) { 
		this.manager = mgr;
	}
	
	/**
	 * Checks if a given user is valid and may proceed registration.
	 * 
	 * Check for valid username/email/password.
	 * 
	 * @param u the user to check
	 * @return whether the check was succesful or not
	 */
	public boolean isValid(@Nonnull User u) {
		if(!emailFormatValid(u.getMail()))
			throw new InvalidEmailException("Invalid email format");
		
		if(!mxRecordExists(u.getMail()))
			throw new InvalidEmailException("Invalid domain provided");
		
		if(u.getUserName() == null || u.getUserName().length() < 3)
			throw new InvalidUserNameException(u.getUserName(), "Username should have at least 3 characters.");
		
		if(userExists(u.getUserName()))
			throw new InvalidUserNameException(u.getUserName(), "User already exists.");
		
		return true;
	}
	
	/**
	 * Taken from http://www.linuxjournal.com/article/9585.
	 * 
	 * @param mail the email address.
	 * @return true if the email format is valid
	 */
	private boolean emailFormatValid(String mail) {
		if(!first.matcher(mail).matches()) return false;
		
		String[] parts = mail.split("@");
		String[] locals = parts[0].split("\\.");
		
		for(String l : locals) {
			if(!second.matcher(l).matches()) return false;
		}
		
		String[] domains = parts[1].split("\\.");
		for(String domain: domains) {
			if(!third.matcher(domain).matches()) return false;
		}
		
		return true;
	}
	
	private boolean mxRecordExists(String mail) {
		try {
			String[] split = mail.split("@");
			if(split.length != 2) return false;
			
			logger.info("Looking up mx record for {}", mail);
			Hashtable<String,String> env = new Hashtable<>();
		    env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
		    DirContext ictx = new InitialDirContext( env );
		    Attributes attrs = ictx.getAttributes(split[1], new String[] { "MX" });
		    Attribute attr = attrs.get( "MX" );
		    if( attr == null ) return false;
		    
		    return true;
		} catch (NamingException ex) {
			logger.fatal("Error looking up mx record", ex);
			return false;
		}
	}
	
	private boolean userExists(String name) {
		return manager.getUser(name) != null;
	}
	
	@CheckReturnValue
	public static UserValidator createDefaultUserValidator(UserManager mgr) {
		return new UserValidator(mgr);
	}
	
	public static class InvalidEmailException extends RuntimeException {
		private static final long serialVersionUID = 9166077884096634612L;

		public InvalidEmailException(String msg) {
			super("Invalid email address provided: " + msg);
		}
	}
	
	public static class InvalidUserNameException extends RuntimeException {
		private static final long serialVersionUID = -740513506940968591L;

		public InvalidUserNameException(String name, String msg) {
			super("Invalid username: " + name + ". " + msg);
		}
	}
}
