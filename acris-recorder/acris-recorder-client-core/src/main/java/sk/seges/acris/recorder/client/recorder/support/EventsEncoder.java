package sk.seges.acris.recorder.client.recorder.support;

import sk.seges.acris.recorder.client.event.encoding.EventEncoder;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericTargetableEvent;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class EventsEncoder {

	public static final char DELIMITER = '\u0001';
	public static final String CHARSET_NAME = "ISO-8859-1";

	public String encodeEvents(List<AbstractGenericEvent> recorderEventsForPersisting) throws UnsupportedEncodingException {

		String result = "";

		List<String> targetXpaths = new ArrayList<String>();

		for (AbstractGenericEvent event : recorderEventsForPersisting) {
			byte[] encodedEvent = EventEncoder.encodeEvent(event);

			String encodedEventString = new String(encodedEvent, CHARSET_NAME);

			result += encodedEventString;

			if (event instanceof AbstractGenericTargetableEvent) {
				String targetXpath = ((AbstractGenericTargetableEvent)event).getRelatedTargetXpath();
				if (targetXpath != null && targetXpaths.contains(targetXpath)) {
					result += ("" + targetXpaths.indexOf(targetXpath));
				} else if (targetXpath != null) {
					targetXpaths.add(targetXpath);
					result += targetXpath;
				}
			}

			result += DELIMITER;
			result += ("" + event.getDeltaTime()) + DELIMITER;
		}

		return result;
	}

}
