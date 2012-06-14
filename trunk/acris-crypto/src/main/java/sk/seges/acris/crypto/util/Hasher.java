package sk.seges.acris.crypto.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import sk.seges.acris.crypto.digest.SHA1Digest;

public class Hasher {
	

	public static byte[] str2intA(String s)  {
		int len = s.length();
		byte[] result = new byte[len];
		
		for(int i = 0; i < len; ++i) {
			char c = s.charAt(i);
			int in = c;
			while(in > 255) {
				in = in >> 1;
			}
			result[i] = (byte) in;
		}
		
		return result;
	}
	
	public static String getSHAHexDigest(String s) {
		if(null == s) {
			throw new IllegalArgumentException();
		}
		
		SHA1Digest digest = new SHA1Digest();
		try {
			byte[] bytes = str2intA(s);
			byte[] output = new byte[20];
			digest.update(bytes, 0, bytes.length);
			digest.doFinal(output, 0);
			return Util.toHexString(output);
		} catch (Exception e) {
			throw new RuntimeException("Failed to compute hash", e);
		}
	}

	public static String getSHA256HexDigest(String password, String salt) {
		if (password == null || salt == null) {
			throw new IllegalArgumentException();
		}
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.reset();
			digest.update(salt.getBytes("UTF-8"));
			return Util.toHexString(digest.digest(password.getBytes("UTF-8")));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Failed to compute hash", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Failed to compute hash", e);
		}
	}

	public static String generateSalt256() {
		try {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

			// Salt generation 256 bits long
			byte[] bSalt = new byte[32];
			random.nextBytes(bSalt);

			return Util.toHexString(bSalt);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Failed to generate salt", e);
		}

	}
}
