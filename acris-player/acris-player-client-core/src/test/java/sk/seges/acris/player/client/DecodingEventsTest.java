package sk.seges.acris.player.client;

import com.google.gwt.dom.client.BrowserEvents;
import junit.framework.Assert;
import org.junit.Test;
import sk.seges.acris.player.client.event.decoding.EventsDecoder;
import sk.seges.acris.player.client.model.HtmlTestEvent;
import sk.seges.acris.player.client.model.KeyboardTestEvent;
import sk.seges.acris.player.client.model.MouseTestEvent;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;
import sk.seges.acris.recorder.client.recorder.support.EventsEncoder;
import sk.seges.acris.recorder.client.tools.CacheMap;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.ArrayList;

public class DecodingEventsTest {

	@Test
	public void testDecodeEvents() throws UnsupportedEncodingException {

		List<AbstractGenericEvent> events = new ArrayList<AbstractGenericEvent>();

		events.add(new HtmlTestEvent(BrowserEvents.ERROR));
		events.add(new HtmlTestEvent(BrowserEvents.FOCUS));
		events.add(new KeyboardTestEvent(BrowserEvents.KEYDOWN));
		events.add(new KeyboardTestEvent(BrowserEvents.KEYDOWN, 33, '6'));
		events.add(new HtmlTestEvent(BrowserEvents.ERROR));
		events.add(new HtmlTestEvent(BrowserEvents.FOCUS));
		events.add(new MouseTestEvent(BrowserEvents.CLICK));
		events.add(new MouseTestEvent(BrowserEvents.MOUSEUP));
		events.add(new MouseTestEvent(BrowserEvents.MOUSEWHEEL));
		events.add(new MouseTestEvent(BrowserEvents.CLICK, 2110, 1400));
		MouseTestEvent mouseTestEvent = new MouseTestEvent(BrowserEvents.MOUSEOUT, 2110, 1400);
		mouseTestEvent.setCtrlKeyInt(1);
		mouseTestEvent.setAltKeyInt(1);
		mouseTestEvent.setRelativeInt(1);
		mouseTestEvent.setRelatedTargetXpath("//test");
		events.add(mouseTestEvent);
		mouseTestEvent = new MouseTestEvent(BrowserEvents.MOUSEOUT, 1110, 1200);
		mouseTestEvent.setRelatedTargetXpath("//test");
		events.add(mouseTestEvent);

		List<AbstractGenericEvent> decodedEvents = new EventsDecoder(new CacheMap(30)).decodeEvents(new EventsEncoder().encodeEvents(events));

		Assert.assertEquals("Events count does not match", events.size(), decodedEvents.size());

		for (int i = 0; i < events.size(); i++) {
			Assert.assertTrue("Event (" + i + ") is not correctly decoded!", events.get(i).equals(decodedEvents.get(i)));
		}
	}
}