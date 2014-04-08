package sk.seges.acris.player.client.event.decoding;

import com.google.gwt.core.client.GWT;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericTargetableEvent;
import sk.seges.acris.recorder.client.recorder.support.EventsEncoder;
import sk.seges.acris.recorder.client.tools.CacheMap;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class EventsDecoder {

	private final CacheMap cacheMap;

	public EventsDecoder(CacheMap cacheMap) {
		this.cacheMap = cacheMap;
	}

	private static byte[] subarray(byte[] bytes, int from, int to) {

		byte[] result = new byte[to - from + 1];

		for (int i = from; i <= to; i++) {
			result[i - from] = bytes[i];
		}

		return result;
	}

	public List<AbstractGenericEvent> decodeEvents(String encodedEvents) throws UnsupportedEncodingException {

		List<AbstractGenericEvent> result = new ArrayList<AbstractGenericEvent>();

		byte[] bytes = encodedEvents.getBytes(EventsEncoder.CHARSET_NAME);

		byte[] eventBytes = null;
		String targetXpath = null;

		List<String> targetXpaths = new ArrayList<String>();

		int lastIndex = 0;
		int length = 0;

        EventDecoder eventDecoder = new EventDecoder(cacheMap);

		for (int i = 0; i < bytes.length; i++) {

			if (lastIndex == i) {
				//the first byte is processed, so we are interested in first bit
				length = bytes[i] < 0 ? 8 : 4;
				continue;
			}

			if (lastIndex + length == i) {
				eventBytes = subarray(bytes, lastIndex, i - 1);
				lastIndex = i;
				length = 0;
			}

			if (bytes[i] == EventsEncoder.DELIMITER) {
				if (targetXpath == null) {

					if (lastIndex == i) {
						targetXpath = "";
					} else {
						targetXpath = new String(subarray(bytes, lastIndex, i - 1), EventsEncoder.CHARSET_NAME);

						try {
							int targetXpathIndex = Integer.valueOf(targetXpath);
							targetXpath = targetXpaths.get(targetXpathIndex);
						} catch (NumberFormatException ex) {
							targetXpaths.add(targetXpath);
						}
					}

					lastIndex = i;
				} else {

					AbstractGenericEvent abstractGenericEvent;

					try {
						result.add(abstractGenericEvent = eventDecoder.decodeEvent(eventBytes));
					} catch (RuntimeException ex) {
						GWT.log(ex.getMessage());
						continue;
					}

					abstractGenericEvent.setDeltaTime((int)EventDecoder.longFromByteArray(subarray(bytes, lastIndex, i - 1)));

					if (abstractGenericEvent != null && abstractGenericEvent instanceof AbstractGenericTargetableEvent && targetXpath.length() > 0) {
						((AbstractGenericTargetableEvent)abstractGenericEvent).setRelatedTargetXpath(targetXpath);
					}

					eventBytes = null;
					targetXpath = null;
					lastIndex = i + 1;
				}
			}
		}

		return result;
	}
}