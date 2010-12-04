package sk.seges.acris.crypto.util;

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
	
	public static void main(String[] args) {
		
		System.out.println(Hasher.getSHAHexDigest("root"));
	}
}
