package sk.seges.acris.player.client;

import junit.framework.Assert;
import sk.seges.acris.player.client.event.decoding.EventDecoder;
import sk.seges.acris.recorder.client.event.encoding.EventEncoder;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;
import sk.seges.acris.recorder.client.tools.ElementXpathCache;

public class AbstractDecodingTest {

	public void testEventDecoding(AbstractGenericEvent event) {
		byte[] bytes = EventEncoder.encodeEvent(event);

		AbstractGenericEvent abstractGenericEvent = new EventDecoder(new ElementXpathCache(30)).decodeEvent(bytes);
		Assert.assertTrue(event.getType() + " event is not correctly decoded!", abstractGenericEvent.equals(event));
	}
}
