package ch.fhnw.apsi.cookies.server.model;

public class ClientSession {
	private final User owner;
	private final String token;
	private final byte[] headerInfoHash;
	private final long timestamp;
	
	private ClientSession(User owner, String token,  byte[] headerInfoHash) {
		this.owner = owner;
		this.token = token;
		this.headerInfoHash = headerInfoHash;
		this.timestamp = System.currentTimeMillis();
	}

	public User getOwner() {
		return owner;
	}

	public String getToken() {
		return token;
	}

	public byte[] getHeaderInfoHash() {
		return headerInfoHash;
	}

	public long getCreationTime() {
		return timestamp;
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
		if (this == obj)		return true;
		if (obj == null)		return false;
		if (getClass() != obj.getClass()) return false;
		
		ClientSession other = (ClientSession) obj;
		if (token == null) {
			if (other.token != null) return false;
		} else if (!token.equals(other.token)) {
			return false;
		}
			
		return true;
	}
	
	public ClientSession createSessionFromRequest(User owner, byte[] headerInfoHash) {
		return new ClientSession(owner, "", headerInfoHash);
	}
}
