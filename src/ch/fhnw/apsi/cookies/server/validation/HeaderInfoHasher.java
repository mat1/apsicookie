package ch.fhnw.apsi.cookies.server.validation;

import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.fhnw.apsi.cookies.server.cookies.Base64;

import com.sun.net.httpserver.Headers;

public final class HeaderInfoHasher {
	private static final Set<String> ignored = new HashSet<>();
	static {
		ignored.add("host");
		ignored.add("cookie");
		ignored.add("cache-control");
		ignored.add("content-length");
		ignored.add("content-type");
		ignored.add("origin");
		ignored.add("connection");
		ignored.add("referer");
	}
	
	private static final Logger logger = LogManager.getLogger(HeaderInfoHasher.class.getName());
	
	private HeaderInfoHasher() {}
	
	public String generateHeaderInfoHash(@Nonnull InetAddress addr, @Nonnull Headers headers) {
		byte[] address = addr.getAddress();
		byte[] heads = concatenateHeader(headers).getBytes();
		byte[] toEncode = new byte[address.length+heads.length];

		int i = 0;
		for(byte b: address) {
			toEncode[i++] = b;
		}
		for(byte b: heads) {
			toEncode[i++] = b;
		}
		
		return Base64.encodeBytes(toHash(toEncode));
	}
	
	private byte[] toHash(byte[] from) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(from);
			return md.digest();
		} catch (NoSuchAlgorithmException ex) {
			logger.fatal("Unable to hash", ex);
			throw new RuntimeException(ex);
		}
	}
	
	private String concatenateHeader(Headers headers) {
		StringBuilder builder = new StringBuilder();
		for(Entry<String, List<String>> e : headers.entrySet()) {
			if(ignored.contains(e.getKey().toLowerCase()))
				continue;
			
			builder.append(e.getKey());
			appendList(builder, e.getValue());
		}
		return builder.toString();
	}
	
	private void appendList(StringBuilder builder, List<String> str) {
		for(String s: str) {
			builder.append(s);
		}
	}
	
	public static HeaderInfoHasher create() {
		return new HeaderInfoHasher();
	}
}
