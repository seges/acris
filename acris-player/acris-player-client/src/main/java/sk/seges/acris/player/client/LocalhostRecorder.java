package sk.seges.acris.player.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.seges.acris.player.client.event.decoding.EventsDecoder;
import sk.seges.acris.player.shared.service.IPlayerRemoteServiceAsync;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;
import sk.seges.acris.recorder.client.recorder.BatchRecorder;
import sk.seges.acris.recorder.client.recorder.support.EventsEncoder;
import sk.seges.acris.recorder.client.tools.ElementXpathCache;
import sk.seges.acris.recorder.shared.model.dto.RecordingLogDTO;
import sk.seges.acris.recorder.shared.model.dto.RecordingSessionDTO;
import sk.seges.sesam.shared.model.dto.PageDTO;
import sk.seges.sesam.shared.model.dto.PagedResultDTO;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by PeterSimun on 2.5.2014.
 */
public class LocalhostRecorder extends BatchRecorder {

    protected LocalhostRecorder(ElementXpathCache elementXpathCache) {
        super(elementXpathCache);
    }

    protected void logEvents(final List<AbstractGenericEvent> recorderEventsForPersisting) {

        EventsEncoder.EncodedEvent encodedEvents;
        try {
            encodedEvents = eventsEncoder.encodeEvents(recorderEventsForPersisting, blobId);
        } catch (UnsupportedEncodingException e) {
            GWT.log(e.getMessage());
            return;
        }

        final List<String> blobs = encodedEvents.blobs;

        IPlayerRemoteServiceAsync playerService = new IPlayerRemoteServiceAsync() {
            @Override
            public void getLogs(PageDTO page, RecordingSessionDTO sessions, AsyncCallback<PagedResultDTO<List<RecordingLogDTO>>> callback) {}

            @Override
            public void getRecodedBlob(long sessionId, long blobId, AsyncCallback<String> callback) {
                callback.onSuccess(blobs.get((int) blobId));
            }

            @Override
            public void getSession(long sessionId, AsyncCallback<RecordingSessionDTO> callback) {}

            @Override
            public void getSessions(PageDTO page, AsyncCallback<PagedResultDTO<List<RecordingSessionDTO>>> callback) {}
        };

        EventsDecoder eventsDecoder = new EventsDecoder(playerService, recordingSession.getId(), elementXpathCache);

        List<AbstractGenericEvent> abstractGenericEvents;
        try {
            abstractGenericEvents = eventsDecoder.decodeEvents(encodedEvents.data);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        boolean sizeEquals = abstractGenericEvents.size() == recorderEventsForPersisting.size();

        int i = 0;
        for (AbstractGenericEvent event: abstractGenericEvents) {
            if (!event.equals(recorderEventsForPersisting.get(i))) {
                AbstractGenericEvent abstractGenericEvent = recorderEventsForPersisting.get(i);
                boolean res = event.equals(abstractGenericEvent);
            }
            i++;
        }

        blobId += encodedEvents.blobs.size();
        recordingLogDTO = null;
    }

    protected void startSession() {
        sessionStarted = true;
        recordingSession.setId(Long.valueOf(Random.nextInt()));
    }

    protected void saveLog(RecordingLogDTO log, long blobId, List<String> blobs) {}
}

