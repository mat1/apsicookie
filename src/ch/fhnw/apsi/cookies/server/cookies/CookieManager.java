package ch.fhnw.apsi.cookies.server.cookies;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * http://www.cse.msu.edu/~alexliu/publications/Cookie/cookie.pdf
 * 
 * @author Florian Luescher
 */
public class CookieManager {
	private static final String AES = "AES";
	private static final String PBKDF2_WITH_HMAC_SHA1 = "PBKDF2WithHmacSHA1";
	
	private static final int ITERATION_COUNT = 65536;
	private static final int KEY_LENGTH = 256;
	private static final int COOKIE_PARTS = 4;
	
	private final byte[] salt = generateKey(8);
	
	public String getCookieString(String token, long expiretime, String data, byte[] sk) {
		byte[] k = hmacEncode(sk, token+String.valueOf(expiretime));
		
		StringBuilder builder = new StringBuilder();
		builder.append(token);
		builder.append(';');
		builder.append(String.valueOf(expiretime));
		builder.append(';');
		builder.append(encrypt(k, data));
		builder.append(';');
		builder.append(Base64.encodeBytes(hmacEncode(k, token+String.valueOf(expiretime)+data)));
		return builder.toString();
	}
	 
	public String extractToken(String cookie) {
		String[] parts = cookie.split(";");
		if(parts.length >= 1) {
			return parts[0];
		}
		
		return "";
	}
	
	public String getCookieData(byte[] sk, String cookie) {
		if(!isValid(sk, cookie)) return null;
		
		String[] parts = cookie.split(";");
		String token = parts[0];
		long time = Long.parseLong(parts[1]);
		String encrypted = parts[2];
		byte[] k = hmacEncode(sk, token+String.valueOf(time));
		
		return decrypt(k, encrypted);
	}
	
	public boolean isValid(byte[] sk, String cookie) {
		String[] parts = cookie.split(";");
		if(parts.length != COOKIE_PARTS)
			return false;
		
		try {
			String token = parts[0];
			long time = Long.parseLong(parts[1]);
			String encrypted = parts[2];
			byte[] check = Base64.decode(parts[3]);
			
			if(time < System.currentTimeMillis())
				return false;
			
			byte[] k = hmacEncode(sk, token+String.valueOf(time));
			
			String data = decrypt(k, encrypted);
			
			byte[] toCheck = hmacEncode(k, token+String.valueOf(time)+data);
			if(toCheck.length != check.length)
				return false;
			
			for(int i = 0; i < toCheck.length; i++) {
				if(toCheck[i] != check[i]) {
					return false;
				}
			}
			
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	public byte[] generateKey(int size) {
		SecureRandom random = new SecureRandom();
		
		byte[] cur = new byte[1];
		byte[] key = new byte[size];
		
		for(int i = 0; i < size; i++) {
			random.nextBytes(cur);
			key[i] = cur[0];
		}
		
		return key;
	}
	
	public byte[] hmacEncode(byte[] key, String data) {
		try {
			SecretKeySpec keySpec = new SecretKeySpec(key, "HmacSHA1");
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(keySpec);
			byte[] res = mac.doFinal(data.getBytes());
			
			return res;
		} catch (java.security.InvalidKeyException | NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex);
		}
	}

	public String encrypt(byte[] key, String data) {
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_WITH_HMAC_SHA1);
			KeySpec spec = new PBEKeySpec(new String(key).toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKey secret = new SecretKeySpec(tmp.getEncoded(), AES);
			
			Cipher c = Cipher.getInstance(AES);
			c.init(Cipher.ENCRYPT_MODE, secret);
			byte[] encrypted = c.doFinal(data.getBytes());
			
			return Base64.encodeBytes(encrypted);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public String decrypt(byte[] key, String data) {
		try {
			byte[] encrypted = Base64.decode(data);
			
			SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_WITH_HMAC_SHA1);
			KeySpec spec = new PBEKeySpec(new String(key).toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKey secret = new SecretKeySpec(tmp.getEncoded(), AES);
			
			Cipher c = Cipher.getInstance(AES);
			c.init(Cipher.DECRYPT_MODE, secret);
			byte[] decrypted = c.doFinal(encrypted);
			
			return new String(decrypted);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
