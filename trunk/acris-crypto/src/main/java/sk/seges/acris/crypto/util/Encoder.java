package sk.seges.acris.crypto.util;

import java.util.Date;

public class Encoder {
    
    public static byte[] encode(String s) {
        return encode(getBytes(s));
    }

    public static String decode(byte[] bytes) {
        return getString(encode(bytes));
    }

    private static byte[] encode(byte[] bytes) {
        int length = bytes.length;
        byte[] result = new byte[length];
        long key = new Date().getTime() / (1000 * 60 * 60 * 24);
        String keyString = Hasher.getSHAHexDigest("" + key);
        byte[] keyBytes = getBytes(keyString);
        for(int i = 0; i < length; ++i) {
            int shift = (i % 8) * 8;
            long multiplier = ((255 << shift) & keyBytes[i % 40]) >> shift;
            result[i] =  (byte) (multiplier ^ bytes[i]);
        }
        return result;
    }
    
    private static byte[] getBytes(String s) {
        int length = s.length();
        byte[] result = new byte[length * 2];
        for(int i = 0; i < length; ++i) {
            char c = s.charAt(i);
            int n = c;
            result[(i*2)] = (byte)(((255 << 8) & n) >> 8);
            result[(i*2)+1] = (byte) (255 & n);
        }
        return result;
    }
    
    private static String getString(byte[] bytes) {
        int length = bytes.length / 2;
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length; ++i) {
            int n = (bytes[i*2] << 8) | bytes[(i*2) + 1];
            char c = (char) n;
            sb.append(c);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String s = "ľščľščľžadfvsdfbtrbnrt4353452345";
        byte[] encoded = Encoder.encode(s);
        System.out.println(getString(encoded));
        String decoded = Encoder.decode(encoded);
        System.out.println(decoded);
    }
}
