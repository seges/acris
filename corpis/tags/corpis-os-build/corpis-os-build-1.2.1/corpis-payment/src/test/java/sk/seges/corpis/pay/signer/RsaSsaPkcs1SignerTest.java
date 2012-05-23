/**
 * 
 */
package sk.seges.corpis.pay.signer;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.security.Security;
import java.util.ArrayList;
import java.util.Collection;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

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
}
