/**
 * 
 */
package sk.seges.corpis.pay.tatra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.junit.Test;

import sk.seges.corpis.domain.shared.pay.PaymentRequest;
import sk.seges.corpis.domain.shared.pay.tatra.TatraPayParameter;
import sk.seges.corpis.domain.shared.pay.tatra.TatraPayRequest;
import sk.seges.corpis.domain.shared.pay.tatra.TatraPaySettings;
import sk.seges.corpis.pay.signer.DESSigner;
import sk.seges.corpis.pay.signer.PaymentSigner;

/**
 * @author ladislav.gazo
 */
public class TatraPayRequestBuilderTest {
	private static final String BASEURL = "http://superbanka.sk/testnima";

	@Test
	public void testBasicRequestBuild() throws Exception {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

		TatraPaySettings settings = new TatraPaySettings();
		settings.setKey("123456789");
		settings.setBaseUrl(BASEURL);
		settings.setMid("08A");
		settings.setRurl("http://hungaropage.hu/");
		
		TatraPayRequest request = new TatraPayRequest();
		request.setSettings(settings);
		request.setAmt(BigDecimal.valueOf(12.7d));
		request.setVs(235664L);
		
		PaymentSigner signer = new DESSigner();
		TatraPayRequestBuilder builder = new TatraPayRequestBuilder(factory.getValidator(), signer);
		PaymentRequest paymentRequest = builder.generate(request);
		
		assertEquals("12.70", paymentRequest.getParameters().get(TatraPayParameter.AMT.getName()));
		assertNotNull("Sign not generated?", paymentRequest.getParameters().get(TatraPayParameter.SIGN.getName()));
	}
}
