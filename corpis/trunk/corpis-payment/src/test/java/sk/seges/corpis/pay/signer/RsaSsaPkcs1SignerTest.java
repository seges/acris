/**
 * 
 */
package sk.seges.corpis.pay.signer;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.junit.Test;

import sk.seges.corpis.pay.SignatureHelper;

/**
 * @author ladislav.gazo
 */
public class RsaSsaPkcs1SignerTest {
	@Test
	public void testSigner() throws Exception {
		Security.addProvider(new BouncyCastleProvider());

		InputStream inStream = RsaSsaPkcs1SignerTest.class.getResourceAsStream("gpg_store/secring.gpg");
		PaymentSigner signer = new RsaSsaPkcs1Signer(inStream, "TestKey", "TestKey");

		Collection<String> parametersInOrder = new ArrayList<String>();
		parametersInOrder.add("Test");
		parametersInOrder.add("Strings");
		assertEquals(Integer.valueOf(512), Integer.valueOf(signer.forgeSignature(null, parametersInOrder)
				.length()));
	}
	
	@Test
	public void testMojdellSigner() throws Exception {
		Security.addProvider(new BouncyCastleProvider());

		InputStream inStream = new FileInputStream("/home/psenicka/.acris-web/gpg/secring.gpg");
//		PaymentSigner signer = new RsaSsaPkcs1Signer(inStream, "BML", "Z4pl4t1m3M0jD3ll");
//
//		Collection<String> parametersInOrder = new ArrayList<String>();
//		parametersInOrder.add("2004bml");
//		parametersInOrder.add("210.72");
//		parametersInOrder.add("21349745");
//		parametersInOrder.add(PaymentConstants.CONSTANT_SYMBOL_DEFAULT);
//		parametersInOrder.add("http://mojdell.sk");
//		
//		String forgeSignature = signer.forgeSignature(null, parametersInOrder);
//		System.out.println(forgeSignature);
//		assertEquals(Integer.valueOf(512), Integer.valueOf(forgeSignature
//				.length()));
		
		PrivateKey privateKey = null;
		try {
			PGPSecretKey readSecretKey = RsaSsaPkcs1Signer.readSecretKey(inStream, "BML <platba@mojdell.sk>");
			PGPPrivateKey extractPrivateKey = readSecretKey.extractPrivateKey("Z4pl4t1m3M0jD3ll".toCharArray(), "BC");
			
			privateKey = extractPrivateKey.getKey();
		} catch (Exception e) {

		}
		
		byte[] signature;
		String paramStr = 
//		"0001FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF" +
//				"FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF" +
//				"FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF" +
//				"FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF" +
//				"FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF" +
//				"FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF" +
//				"FFFFFFFFFFFFFFFFFFFFFFFF003031300d060960864801650304020105000420" +
//				"76E5F894DDD8F0CF120EA38BCD3BB5AE3646D01C225C0AB2E0F9617C56ACC59E";
				
				"3031300D060960864801650304020105000420" +
				"76E5F894DDD8F0CF120EA38BCD3BB5AE3646D01C225C0AB2E0F9617C56ACC59E";
				
//		"2004bml210.72213497450308http://mojdell.sk";
		System.out.println("3031300D060960864801650304020105000420" +
				"76E5F894DDD8F0CF120EA38BCD3BB5AE3646D01C225C0AB2E0F9617C56ACC59E");
//		MessageDigest md = MessageDigest.getInstance("SHA-256");
//		md.update(paramStr.getBytes());
//		byte[] mdbytes = md.digest();
//        
//        AlgorithmIdentifier algId = new AlgorithmIdentifier(new DERObjectIdentifier("1.3.6.1.4.1.22554.2"), DERNull.INSTANCE);
//        DigestInfo dInfo = new DigestInfo(algId, mdbytes);
//        byte[] derder = dInfo.getEncoded("DER");
//        System.out.println(SignatureHelper.byteArrayToHexString(derder));
//
        InputStream publicKeyStream = new FileInputStream("/home/psenicka/.acris-web/gpg/pubring.gpg");
        PGPPublicKey rsaPublicKey = readPublicKeyFromCol(publicKeyStream, "BML <platba@mojdell.sk>");

        byte[] input = hexStringToByteArray(paramStr);
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        rsaCipher.init(Cipher.ENCRYPT_MODE, privateKey);

        byte[] encodedKeyBytes = rsaCipher.doFinal(input); 
        System.out.println("cipher: " + SignatureHelper.byteArrayToHexString(encodedKeyBytes));

        rsaCipher = Cipher.getInstance("RSA/ECB/NoPadding"); 
        rsaCipher.init(Cipher.DECRYPT_MODE, rsaPublicKey.getKey("BC")); 
        byte[] decodedResult = rsaCipher.doFinal(encodedKeyBytes);
        System.out.println("cipher: " + SignatureHelper.byteArrayToHexString(decodedResult));
      
        
        PublicKey publicKey = rsaPublicKey.getKey("BC");
        
		try {
			Signature mySigner = Signature.getInstance("SHA256withRSA");
			mySigner.initSign(privateKey);
			mySigner.update("2004bml210.72213497450308http://mojdell.sk".getBytes());
			signature = mySigner.sign();
		} catch (GeneralSecurityException e) {
			throw new RuntimeException("Unable to create signature from params = " + paramStr, e);
		}
		String hexString = SignatureHelper.byteArrayToHexString(signature);	
		System.out.println(hexString);
		
		rsaCipher = Cipher.getInstance("RSA/ECB/NoPadding"); 
        rsaCipher.init(Cipher.DECRYPT_MODE,publicKey); 
        decodedResult = rsaCipher.doFinal(hexStringToByteArray(hexString));
        System.out.println("cipher: " + SignatureHelper.byteArrayToHexString(decodedResult));
 	}	
	
	public static byte[] hexStringToByteArray(String s) {
	    byte[] b = new byte[s.length() / 2];
	    for (int i = 0; i < b.length; i++) {
	      int index = i * 2;
	      int v = Integer.parseInt(s.substring(index, index + 2), 16);
	      b[i] = (byte) v;
	    }
	    return b;
	  }
	
    private static PGPPublicKey readPublicKeyFromCol(InputStream in, String keyRing)
            throws Exception {
     PGPPublicKeyRing pkRing = null;
     PGPPublicKeyRingCollection pkCol = new PGPPublicKeyRingCollection(in);
     System.out.println("key ring size=" + pkCol.size());
     Iterator<?> it = pkCol.getKeyRings(keyRing);
     while (it.hasNext()) {
             pkRing = (PGPPublicKeyRing) it.next();
             Iterator<?> pkIt = pkRing.getPublicKeys();
             while (pkIt.hasNext()) {
                     PGPPublicKey key = (PGPPublicKey) pkIt.next();
                     System.out.println("Encryption key = " + key.isEncryptionKey() + ", Master key = " + 
                                        key.isMasterKey());
                     if (key.isEncryptionKey())
                             return key;
             }
     }
     return null;
}	
	

	

}
