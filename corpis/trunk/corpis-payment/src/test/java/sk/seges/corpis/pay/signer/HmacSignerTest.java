/**
 * 
 */
package sk.seges.corpis.pay.signer;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;

import sk.seges.corpis.domain.pay.vub.VUBePlatbaSettings;

/**
 * @author ladislav.gazo
 */
public class HmacSignerTest {
	/**
	 * this test cannot be executed because we cannot commit real data ;)
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testSigner() throws Exception {
		PaymentSigner signer = new HmacSigner();
		
		
		Collection<String> parametersInOrder = new ArrayList<String>();
		parametersInOrder.add("xxx");
		parametersInOrder.add("1");
		parametersInOrder.add("123");
		parametersInOrder.add("0308");
		parametersInOrder.add("http://xyz.sk");
		
		VUBePlatbaSettings settings = new VUBePlatbaSettings();
		settings.setBaseUrl("https://zyx.sk");
		settings.setKey("123");
		settings.setMid("xxx");
		settings.setRem("bank@address.com");
		settings.setRurl("http://ll.sk");
		
		String signature = signer.forgeSignature(settings, parametersInOrder);
		System.out.println(signature);
		assertEquals(Integer.valueOf(64), Integer.valueOf(signature.length()));
	}


}
