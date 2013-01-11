package ch.fhnw.apsi.cookies.server;

import org.junit.Before;
import org.junit.Test;

import ch.fhnw.apsi.cookies.server.validation.UserValidator.InvalidUserNameException;

public class UserManagerTest {

	private UserManager underTest;
	
	@Before
	public void setUp() {
		underTest = new UserManager();
	}
	
	@Test(expected=InvalidUserNameException.class)
	public void testUserAlreadyExists() {
		underTest.createUser("florian", "florian.luescher@students.fhnw.ch");
		underTest.createUser("florian", "florian.luescher@students.fhnw.ch");
	}

}
