package ch.fhnw.apsi.cookies.server.validation;

import java.util.List;
import java.util.Map.Entry;

import com.sun.net.httpserver.Headers;

public class HeaderInfoHasher {
	public byte[] generateHeaderInfoHash(Headers headers) {
		return concatenateHeader(headers).getBytes();
	}
	
	private String concatenateHeader(Headers headers) {
		StringBuilder builder = new StringBuilder();
		for(Entry<String, List<String>> e : headers.entrySet()) {
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
}
