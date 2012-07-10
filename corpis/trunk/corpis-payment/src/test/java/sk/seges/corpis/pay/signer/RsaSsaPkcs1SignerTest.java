/**
 * 
 */
package sk.seges.corpis.pay.signer;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;
import org.junit.Test;

import sk.seges.corpis.pay.SignatureHelper;

/**
 * @author ladislav.gazo
 */
public class RsaSsaPkcs1SignerTest {
	@Test
	public void testMDSigner() throws Exception {
		Security.addProvider(new BouncyCastleProvider());

		InputStream inStream = new FileInputStream("/tmp/secring.gpg");
		PaymentSigner signer = new RsaSsaPkcs1Signer(inStream, "XXX <xxx@yyy.sk>", "uzasneheslo");
		
		
		Collection<String> parametersInOrder = new ArrayList<String>();
		parametersInOrder.add("KKKKKKKK");
		parametersInOrder.add("1");
		parametersInOrder.add("123");
		parametersInOrder.add("0308");
		parametersInOrder.add("http://abc");
		
//		AsymmetricBlockCipher cipher = new RSAEngine();
//		PKCS1Encoding encoding = new PKCS1Encoding(cipher);
		
		String signature = signer.forgeSignature(null, parametersInOrder);
		System.out.println(signature);
		assertEquals(Integer.valueOf(512), Integer.valueOf(signature.length()));
		
		verify("KKKKKKKK11230308http://abc", signature);
		
		
	}

	public void verify(String signatureBaseString, String signature) throws Exception {
		FileInputStream securityKeyRing = new FileInputStream(new File("/tmp/pubring.gpg"));
		PGPPublicKey pgppublicKey = readPublicKey(securityKeyRing, "XXX <xxx@yyy.sk>");
		PublicKey publicKey = pgppublicKey.getKey("BC");

		byte[] signatureBytes = SignatureHelper.toByteArray(signature);

		Signature verifier = Signature.getInstance("SHA256withRSA");
		verifier.initVerify(publicKey);
		verifier.update(signatureBaseString.getBytes("UTF-8"));
		if (!verifier.verify(signatureBytes)) {
			throw new RuntimeException("Invalid signature for signature method");
		}
	}
	
	  public static PrivateKey createPrivateKey(byte[] privateKey) {
		    if (privateKey == null) {
		      return null;
		    }

		    try {
		      KeyFactory fac = KeyFactory.getInstance("RSA");
		      EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKey);
		      return fac.generatePrivate(spec);
		    }
		    catch (NoSuchAlgorithmException e) {
		      throw new IllegalStateException(e);
		    }
		    catch (InvalidKeySpecException e) {
		      throw new IllegalStateException(e);
		    }
		  }
	
		@SuppressWarnings("rawtypes")
		public static PGPPublicKey readPublicKey(InputStream in, String keyRingName) throws IOException, PGPException {
			in = PGPUtil.getDecoderStream(in);

			PGPPublicKeyRingCollection pgpSec = new PGPPublicKeyRingCollection(in);

			//
			// we just loop through the collection till we find a key suitable for
			// encryption, in the real
			// world you would probably want to be a bit smarter about this.
			//
			PGPPublicKey key = null;

			//
			// iterate through the key rings.
			//
			Iterator rIt = pgpSec.getKeyRings(keyRingName, true);

			while (key == null && rIt.hasNext()) {
				PGPPublicKeyRing kRing = (PGPPublicKeyRing) rIt.next();
				Iterator kIt = kRing.getPublicKeys();

				while (key == null && kIt.hasNext()) {
					PGPPublicKey k = (PGPPublicKey) kIt.next();
					Iterator userIDs = k.getUserIDs();
					String uid = "NA";
					if (userIDs.hasNext()) {
						uid = (String) userIDs.next();
						System.out.println("Key ring uid = " + uid);
						if(uid.equals(keyRingName)) {
							key = k;
						}
					}
					
				}
			}

			if (key == null) {
				throw new IllegalArgumentException("Can't find signing key in key ring.");
			}
			return key;
		}
	  
	  
//	@Test
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
	

	
	public static byte[] hexStringToByteArray(String s) {
	    byte[] b = new byte[s.length() / 2];
	    for (int i = 0; i < b.length; i++) {
	      int index = i * 2;
	      int v = Integer.parseInt(s.substring(index, index + 2), 16);
	      b[i] = (byte) v;
	    }
	    return b;
	  }
	
    @SuppressWarnings("unused")
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
