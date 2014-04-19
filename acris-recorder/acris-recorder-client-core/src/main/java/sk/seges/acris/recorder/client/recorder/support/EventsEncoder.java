package sk.seges.acris.recorder.client.recorder.support;

import sk.seges.acris.recorder.client.event.ClipboardEvent;
import sk.seges.acris.recorder.client.event.encoding.EventEncoder;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericTargetableEvent;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class EventsEncoder {

	public static final char DELIMITER = '\u0001';
    public static final String DATA_ATTRIBUTE = "d:";
    public static final String BLOB_ATTRIBUTE = "b:";
	public static final String CHARSET_NAME = "ISO-8859-1";

    public class EncodedEvent {
        public String data;
        public List<String> blobs = new ArrayList<String>();
    }

	public EncodedEvent encodeEvents(List<AbstractGenericEvent> recorderEventsForPersisting, long blobId) throws UnsupportedEncodingException {

		String data = "";

		List<String> targetXpaths = new ArrayList<String>();

        EncodedEvent result = new EncodedEvent();

		for (AbstractGenericEvent event : recorderEventsForPersisting) {
			byte[] encodedEvent = EventEncoder.encodeEvent(event);

			String encodedEventString = new String(encodedEvent, CHARSET_NAME);

			data += encodedEventString;

            if (event instanceof ClipboardEvent) {
                ClipboardEvent clipboardEvent = ((ClipboardEvent)event);
                if (clipboardEvent.getClipboardText() != null && clipboardEvent.getClipboardText().length() > 100) {
                    result.blobs.add(clipboardEvent.getClipboardText());
                    data += BLOB_ATTRIBUTE + (result.blobs.size() + blobId);
                } else {
                    if (clipboardEvent.getClipboardText() != null) {
                        data += DATA_ATTRIBUTE + clipboardEvent.getClipboardText();
                    }
                }
                data += DELIMITER;
            }

			if (event instanceof AbstractGenericTargetableEvent) {
				String targetXpath = ((AbstractGenericTargetableEvent)event).getRelatedTargetXpath();
				if (targetXpath != null && targetXpaths.contains(targetXpath)) {
					data += ("" + targetXpaths.indexOf(targetXpath));
				} else if (targetXpath != null) {
					targetXpaths.add(targetXpath);
					data += targetXpath;
				}
			}

			data += DELIMITER;
			data += ("" + event.getDeltaTime()) + DELIMITER;
		}

        result.data = data;

		return result;
	}
}