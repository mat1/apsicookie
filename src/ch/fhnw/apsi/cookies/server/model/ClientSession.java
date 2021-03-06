package ch.fhnw.apsi.cookies.server.model;

import javax.annotation.Nonnull;

public final class ClientSession {
	private final User owner;

	private String token;
	private long expiringTime;
	private byte[] currentKey;
	
	private ClientSession(@Nonnull User owner, @Nonnull String token, long expiringTime, byte[] key) {
		this.owner = owner;
		this.token = token;
		this.expiringTime = expiringTime;
		this.currentKey = key;
	}

	public User getOwner() {
		return owner;
	}

	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public long getExpiringTime() {
		return expiringTime;
	}
	
	public void setExpiringTime(long n) {
		expiringTime = n;
	}
	
	public byte[] getCurrentKey() {
		return currentKey;
	}
	
	public void setCurrentKey(byte[] k) {
		currentKey = k;
	}
	
	public void updateToken(String newToken) {
		token = newToken;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		ClientSession other = (ClientSession) obj;
		if (token == null) {
			if (other.token != null) return false;
		} else if (!token.equals(other.token)) {
			return false;
		}
			
		return true;
	}
	
	public static ClientSession createSession(User owner, String starttoken, long expiringTime, byte[] key) {
		return new ClientSession(owner, starttoken, expiringTime, key);
	}
}
