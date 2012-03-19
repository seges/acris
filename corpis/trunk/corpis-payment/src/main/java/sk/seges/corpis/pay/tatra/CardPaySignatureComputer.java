/**
 * 
 */
package sk.seges.corpis.pay.tatra;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.log4j.Logger;

/**
 * @author spok
 * 
 */
public class CardPaySignatureComputer {
    
    private static final Logger LOGGER = Logger
	    .getLogger(CardPaySignatureComputer.class);

    /**
     * Create signature (SIGN) for tatrapay
     * 
     * @param mid
     * @param amt
     * @param curr
     * @param vs
     * @param cs
     * @param rurl
     * @param ip
     * @param name
     * @param key
     * @return
     */
    public String compute(String mid, String amt, String curr, String vs,
	    String cs, String rurl, String ip, String name, final String key) {

	return byteArrayToHexString(Arrays.copyOf(des(key, Arrays
		.copyOf(
		sha1(mid + amt + curr + vs + cs + rurl + ip + name), 8)), 8));
    }
    
    public String compute(String vs, String res, String ac, final String key) {
    	
    	if(ac != null && ac.length() != 0 ){
    		return byteArrayToHexString(Arrays.copyOf(des(key, Arrays
    	    		.copyOf(
    	    		sha1( vs + res + ac), 8)), 8));
    	}else {
    		return byteArrayToHexString(Arrays.copyOf(des(key, Arrays
    	    		.copyOf(
    	    		sha1( vs + res ), 8)), 8));
    	}
   }

    /**
     * @param in
     * @return
     */
    private byte[] sha1(String in) {
	try {
	    MessageDigest sha = MessageDigest.getInstance("SHA-1");
	    sha.update(in.getBytes());
	    return sha.digest();
	} catch (NoSuchAlgorithmException e) {
	    LOGGER.error(e);
	}
	return null;
    }

    /**
     * @param bytes
     * @return
     */
    private String byteArrayToHexString(byte[] bytes) {
	StringBuilder builder = new StringBuilder(30);
	String str;
	for (int i = 0; i < bytes.length; i++) {
	    str = Integer.toHexString(bytes[i] & 0xFF);
	    if (str.length() == 1) {
		builder.append("0" + str);
	    } else {
		builder.append(str);
	    }
	}
	return builder.toString().toUpperCase();
    }

    /**
     * @param key
     * @param hash
     * @return
     */
    private byte[] des(String key, byte[] hash) {
	try {
	    DESKeySpec desKeySpec;
	    desKeySpec = new DESKeySpec(key.getBytes());
	    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	    SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
	    Cipher ecipher = Cipher.getInstance("DES");
	    ecipher.init(Cipher.ENCRYPT_MODE, secretKey);
	    return ecipher.doFinal(hash);
	} catch (InvalidKeyException e) {
	    LOGGER.error(e);
	} catch (NoSuchAlgorithmException e) {
	    LOGGER.error(e);
	} catch (InvalidKeySpecException e) {
	    LOGGER.error(e);
	} catch (NoSuchPaddingException e) {
	    LOGGER.error(e);
	} catch (IllegalBlockSizeException e) {
	    LOGGER.error(e);
	} catch (BadPaddingException e) {
	    LOGGER.error(e);
	}
	return null;
    }

}
