package ch.fhnw.apsi.cookies.server.validation;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ch.fhnw.apsi.cookies.server.UserManager;
import ch.fhnw.apsi.cookies.server.model.User;
import ch.fhnw.apsi.cookies.server.validation.UserValidator.InvalidEmailException;

public class UserValidatorTest {

	private UserValidator underTest;
	
	@Before
	public void setUp() {
		underTest = UserValidator.createDefaultUserValidator(new UserManager());
	}
	
	@Test
	public void testSuccess() {
		assertTrue(underTest.isValid(User.createUser("florian", "florian.luescher@students.fhnw.ch")));
		assertTrue(underTest.isValid(User.createUser("matthias", "matthias.brun@students.fhnw.ch")));
	}
	
	@Test(expected=InvalidEmailException.class)
	public void testInvalidDomain() {
		underTest.isValid(User.createUser("florian", "blobb.blobb@blobber.blobb"));
	}
	
	@Test(expected=InvalidEmailException.class)
	public void testInvalidMailFormat() {
		underTest.isValid(User.createUser("florian", "blobb.blobb@blobber"));
	}

	@Test(expected=InvalidEmailException.class)
	public void testInvalidMailFormat2() {
		underTest.isValid(User.createUser("florian", "@students.fhnw.ch"));
	}
	
	@Test(expected=InvalidEmailException.class)
	public void testInvalidMailFormat3() {
		underTest.isValid(User.createUser("florian", "@.blobb"));
	}
	
	@Test(expected=InvalidEmailException.class)
	public void testInvalidMailFormat4() {
		underTest.isValid(User.createUser("florian", "@a."));
	}
	
}
