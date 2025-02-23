package security;

import java.security.*;
public class Encrypt {
	public String encrypt(String password) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());
			result += bytesToHex(md.digest());
			System.out.println(result);
			return result;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String bytesToHex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for(byte b : bytes) {
			builder.append(String.format("%02x",b));
		}
		return builder.toString();
	}
}
