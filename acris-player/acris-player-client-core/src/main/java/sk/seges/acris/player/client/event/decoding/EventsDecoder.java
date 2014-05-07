package sk.seges.acris.player.client.event.decoding;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.seges.acris.player.shared.service.IPlayerRemoteServiceAsync;
import sk.seges.acris.recorder.client.event.ClipboardEvent;
import sk.seges.acris.recorder.client.event.EventType;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericTargetableEvent;
import sk.seges.acris.recorder.client.recorder.support.EventsEncoder;
import sk.seges.acris.recorder.client.tools.ElementXpathCache;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class EventsDecoder {

	private final ElementXpathCache elementXpathCache;
    private final IPlayerRemoteServiceAsync playerService;
    private final Long sessionId;

    class ClipboardContentCallback {

        private ClipboardEvent event;
        private String result;

        public void setResult(String result) {
            if (event == null) {
                this.result = result;
            } else {
                event.setClipboardText(result);
            }
        }

        public void setEvent(ClipboardEvent event) {
            this.event = event;
            if (result != null) {
                this.event.setClipboardText(result);
            }
        }

    }

	public EventsDecoder(IPlayerRemoteServiceAsync playerService, Long sessionId, ElementXpathCache elementXpathCache) {
		this.elementXpathCache = elementXpathCache;
        this.playerService = playerService;
        this.sessionId = sessionId;
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

        byte[] isoBytes = encodedEvents.getBytes(EventsEncoder.CHARSET_NAME);

		byte[] eventBytes = null;
		String targetXpath = null;

		List<String> targetXpaths = new ArrayList<String>();

		int lastIndex = 0;
		int length = 0;

        EventDecoder eventDecoder = new EventDecoder(elementXpathCache);

        EventType eventType = null;
        String additionalInfo = null;

        ClipboardContentCallback clipboardCallback = null;

        int j = 0;

		for (int i = 0; i < isoBytes.length; i++) {

			if (lastIndex == i) {
				//the first byte is processed, so we are interested in first bit
				length = isoBytes[i] < 0 ? 8 : 4;
				continue;
			}

			if (lastIndex + length == i) {
				eventBytes = subarray(isoBytes, lastIndex, i - 1);
                lastIndex = i;
				length = 0;

                eventType = EventDecoder.getEventType(eventBytes);
            }

			if (isoBytes[i] == EventsEncoder.DELIMITER) {

                if (eventType.equals(EventType.CustomEvent) && additionalInfo == null) {
                    //there is also blob or pasted text encoded, so 1 more delimiter
                    if (lastIndex == i) {
                        additionalInfo = "";
                    } else {
                        additionalInfo = encodedEvents.substring(lastIndex, i);
                    }

                    //when length == 0 it was cut event, or pasted empty text
                    if (additionalInfo.length() > 0) {
                        //reference to the blob ID
                        if (additionalInfo.startsWith(EventsEncoder.BLOB_ATTRIBUTE)) {
                            final ClipboardContentCallback clipboardAsyncCallback = clipboardCallback = new ClipboardContentCallback();

                            long blobId;
                            String blobStringIdentifier = additionalInfo.substring(EventsEncoder.BLOB_ATTRIBUTE.length());
                            try {
                                blobId = Long.parseLong(blobStringIdentifier);
                            } catch (NumberFormatException nfe) {
                                throw new RuntimeException("Unable to parse blob identifier: " + blobStringIdentifier);
                            }

                            //TODO set clipboard event as non initialized
                            //and playlist should wait until event is initialized
                            playerService.getRecodedBlob(sessionId, blobId, new AsyncCallback<String>() {

                                @Override
                                public void onFailure(Throwable caught) {
                                    throw new RuntimeException(caught);
                                }

                                @Override
                                public void onSuccess(String result) {
                                    clipboardAsyncCallback.setResult(result);
                                }
                            });
                        } else if (additionalInfo.startsWith(EventsEncoder.DATA_ATTRIBUTE)) {
                            //pasted text is encoded in event itself
                            clipboardCallback = new ClipboardContentCallback();
                            clipboardCallback.setResult(additionalInfo.substring(EventsEncoder.DATA_ATTRIBUTE.length()));
                        } else {
                            throw new RuntimeException("Invalid clipboard event!");
                        }
                    }

                    lastIndex = i;
                } else if (targetXpath == null) {

					if (lastIndex == i) {
						targetXpath = "";
					} else {
                        int startIndex = lastIndex + (additionalInfo != null ? 1 : 0);
                        if (startIndex < i) {
                            targetXpath = encodedEvents.substring(startIndex, i);
                        } else {
                            targetXpath = "";
                        }
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

                    j++;
                    if (clipboardCallback != null) {
                        clipboardCallback.setEvent((ClipboardEvent) abstractGenericEvent);
                    }

                    abstractGenericEvent.setDeltaTime(Integer.valueOf(encodedEvents.substring(lastIndex + 1, i - 1)));

					if (abstractGenericEvent != null && abstractGenericEvent instanceof AbstractGenericTargetableEvent && targetXpath.length() > 0) {
						((AbstractGenericTargetableEvent)abstractGenericEvent).setRelatedTargetXpath(targetXpath);
					}

					eventBytes = null;
					targetXpath = null;
					lastIndex = i + 1;
                    clipboardCallback = null;
                    additionalInfo = null;
				}
			}
		}

		return result;
	}
}