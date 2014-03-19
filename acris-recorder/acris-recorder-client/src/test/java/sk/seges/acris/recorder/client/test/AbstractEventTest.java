package sk.seges.acris.recorder.client.test;

import junit.framework.Assert;
import sk.seges.acris.recorder.client.event.encoding.EventEncoder;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;

public class AbstractEventTest {

	protected String getBinaryString(String text) {
		return text.substring(text.length() - 8);
	}

	protected String toBinaryString(byte[] bytes) {
		String result = "";

		for (byte b: bytes) {
			result += getBinaryString("000000000" + Integer.toBinaryString(b));
		}

		return result;
	}

	protected void testEvent(AbstractGenericEvent event, String expectedResult) {
		byte[] bytes = EventEncoder.encodeEvent(event);
		Assert.assertEquals(event.getType() + " event is not properly encoded", expectedResult, toBinaryString(bytes));
	}

}
