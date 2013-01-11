package ch.fhnw.apsi.cookies.server.model;

public class User {
	private final String userName;
	private final String mail;
	
	private User(String userName, String mail) {
		this.userName= userName;
		this.mail = mail;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getMail() {
		return mail;
	}
	
	
	public static User createUser(String name, String mail) {
		return new User(name, mail);
	}
}
