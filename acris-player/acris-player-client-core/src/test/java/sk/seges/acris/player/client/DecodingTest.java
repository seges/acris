package sk.seges.acris.player.client;

import com.google.gwt.dom.client.BrowserEvents;
import junit.framework.Assert;
import org.junit.Test;
import sk.seges.acris.player.client.event.decoding.EventDecoder;
import sk.seges.acris.player.client.model.HtmlTestEvent;
import sk.seges.acris.recorder.client.event.HtmlEvent;
import sk.seges.acris.recorder.client.event.encoding.EventEncoder;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;

public class DecodingTest {

	@Test
	public void testDecode() {
		HtmlEvent htmlEvent = new HtmlTestEvent(BrowserEvents.BLUR);

		byte[] bytes = EventEncoder.encodeEvent(htmlEvent);

		AbstractGenericEvent abstractGenericEvent = EventDecoder.decodeEvent(bytes);
		Assert.assertEquals(abstractGenericEvent, htmlEvent);
	}
}