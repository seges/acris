/**
 * 
 */
package sk.seges.corpis.pay.vub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.math.BigDecimal;
import java.security.Security;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import sk.seges.corpis.domain.pay.PaymentRequest;
import sk.seges.corpis.domain.pay.vub.VUBePlatbaParameter;
import sk.seges.corpis.domain.pay.vub.VUBePlatbaRequest;
import sk.seges.corpis.domain.pay.vub.VUBePlatbaSettings;
import sk.seges.corpis.pay.signer.PaymentSigner;
import sk.seges.corpis.pay.signer.RsaSsaPkcs1Signer;
import sk.seges.corpis.pay.signer.RsaSsaPkcs1SignerTest;

/**
 * @author ladislav.gazo
 */
public class VUBePlatbaRequestBuilderTest {
	private static final String BASEURL = "https://ib.vub.sk/e-platbyeuro.aspx";

	@Test
	public void testBasicRequestBuild() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

		VUBePlatbaSettings settings = new VUBePlatbaSettings();
		settings.setKey("123456789");
		settings.setBaseUrl(BASEURL);
		settings.setMid("trala");
		settings.setRurl("http://hungaropage.hu/");
		
		VUBePlatbaRequest request = new VUBePlatbaRequest();
		request.setSettings(settings);
		request.setAmt(BigDecimal.valueOf(12.7d));
		request.setVs(235664L);
		
		InputStream inStream = RsaSsaPkcs1SignerTest.class.getResourceAsStream("gpg_store/secring.gpg");
		PaymentSigner signer = new RsaSsaPkcs1Signer(inStream, "TestKey", "TestKey");
		
		VUBePlatbaRequestBuilder builder = new VUBePlatbaRequestBuilder(factory.getValidator(), signer);
		PaymentRequest paymentRequest = builder.generate(request);
		
		assertEquals("12.70", paymentRequest.getParameters().get(VUBePlatbaParameter.AMT.getName()));
		assertNotNull("Sign not generated?", paymentRequest.getParameters().get(VUBePlatbaParameter.SIGN.getName()));
	}
}
