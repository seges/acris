/**
 * 
 */
package sk.seges.corpis.pay.signer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;

import sk.seges.corpis.domain.pay.PaymentMethodSettings;
import sk.seges.corpis.pay.SignatureHelper;

/**
 * @author ladislav.gazo
 */
public class RsaSsaPkcs1Signer implements PaymentSigner {
	private static final Logger log = Logger.getLogger(RsaSsaPkcs1Signer.class);
	
	private PrivateKey privateKey;

	public RsaSsaPkcs1Signer(String securityKeyRing, String keyRingName,
			String password) throws FileNotFoundException {
		this(new FileInputStream(new File(securityKeyRing)), keyRingName, password);
	}
	
	public RsaSsaPkcs1Signer(InputStream securityKeyRing, String keyRingName,
			String password) {
		try {
			PGPSecretKey readSecretKey = readSecretKey(securityKeyRing, keyRingName);
			PGPPrivateKey extractPrivateKey = readSecretKey.extractPrivateKey(password.toCharArray(), "BC");
			privateKey = extractPrivateKey.getKey();
		} catch (Exception e) {
			throw new RuntimeException("Unable to initialize builder where key ring = " + securityKeyRing
					+ ", name = " + keyRingName, e);
		}
	}

	@Override
	public String forgeSignature(PaymentMethodSettings settings, Collection<String> parametersInOrder) {
		StringBuilder builder = new StringBuilder();
		for (String param : parametersInOrder) {
			if (param != null) {
				builder.append(param);
			}
		}

		byte[] signature;
		String paramStr = builder.toString();
		try {
			Signature signer = Signature.getInstance("SHA256withRSA");
			signer.initSign(privateKey);
			signer.update(paramStr.getBytes());
			signature = signer.sign();
		} catch (GeneralSecurityException e) {
			throw new RuntimeException("Unable to create signature from params = " + paramStr, e);
		}
		String hexString = SignatureHelper.byteArrayToHexString(signature);

		return hexString;
	}

	@SuppressWarnings( { "unchecked" })
	private static PGPSecretKey readSecretKey(InputStream in, String keyRingName) throws IOException,
			PGPException {
		in = PGPUtil.getDecoderStream(in);

		PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(in);

		//
		// we just loop through the collection till we find a key suitable for
		// encryption, in the real
		// world you would probably want to be a bit smarter about this.
		//
		PGPSecretKey key = null;

		//
		// iterate through the key rings.
		//
		Iterator rIt = pgpSec.getKeyRings(keyRingName, true);

		while (key == null && rIt.hasNext()) {
			PGPSecretKeyRing kRing = (PGPSecretKeyRing) rIt.next();
			Iterator kIt = kRing.getSecretKeys();

			while (key == null && kIt.hasNext()) {
				PGPSecretKey k = (PGPSecretKey) kIt.next();
				Iterator userIDs = k.getUserIDs();
				String uid = "NA";
				if (userIDs.hasNext()) {
					uid = (String) userIDs.next();
					if(log.isDebugEnabled()) {
						log.debug("Key ring uid = " + uid);
					}
				}
				if (k.isSigningKey()) {
					if(log.isDebugEnabled()) {
						log.debug("Key ring signing = " + uid + ", k = " + k);
					}
					key = k;
				}
			}
		}

		if (key == null) {
			throw new IllegalArgumentException("Can't find signing key in key ring.");
		}

		return key;
	}
}
