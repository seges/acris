package sk.seges.acris.recorder.client.recorder.support;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;
import sk.seges.acris.recorder.client.listener.RecorderListener;
import sk.seges.acris.recorder.client.session.RecordingSessionProvider;
import sk.seges.acris.recorder.client.tools.ElementXpathCache;
import sk.seges.acris.recorder.shared.model.dto.RecordingLogDTO;
import sk.seges.acris.recorder.shared.model.dto.RecordingSessionDTO;
import sk.seges.acris.recorder.shared.params.RecordingSessionDetailParams;
import sk.seges.acris.recorder.shared.service.IRecordingRemoteService;
import sk.seges.acris.recorder.shared.service.IRecordingRemoteServiceAsync;
import sk.seges.acris.recorder.shared.service.ServicesDefinition;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

abstract public class Recorder extends AbstractRecorder implements RecorderListener {
	
	protected final RecorderMode mode;
	
	protected final List<AbstractGenericEvent> recorderEvents = new ArrayList<AbstractGenericEvent>();
    protected final EventsEncoder eventsEncoder = new EventsEncoder();

    protected IRecordingRemoteServiceAsync recordingService;

    protected RecordingSessionDTO recordingSession;
    protected RecordingLogDTO recordingLogDTO;

    protected boolean sessionStarted = false;
    protected List<AwaitingLog> awaitingLogs = new ArrayList<AwaitingLog>();
    protected long lastTime;
    protected long blobId = 0;

    public class AwaitingLog {
        RecordingLogDTO log;
        List<String> blobs;
        long blobId;

        public AwaitingLog() {};
        public AwaitingLog(RecordingLogDTO log, long blobId, List<String> blobs) {
            this.log = log;
            this.blobs = blobs;
            this.blobId = blobId;
        }
    }

	protected Recorder(ElementXpathCache elementXpathCache, RecorderMode mode) {
		super(elementXpathCache);
		this.mode = mode;

		this.recordingSession = new RecordingSessionDTO();

		this.recordingSession.setSessionTime(new Date());
		this.lastTime = new Date().getTime();
		this.recordingSession.setLanguage(getDefaultLocale());
		this.recordingSession.setWebId(getWebId());

		RecordingSessionProvider.getSession(new AsyncCallback<RecordingSessionDetailParams>() {
			@Override
			public void onFailure(Throwable caught) {
                GWT.log(caught.getMessage());
                startSession();
			}

			@Override
			public void onSuccess(RecordingSessionDetailParams result) {
				recordingSession.setSessionInfo(result.toString());
                startSession();
			}
		});

		addRecordListener(this);
		initializeService();
	}

    protected void startSession() {
        recordingService.startSession(recordingSession, new AsyncCallback<RecordingSessionDTO>() {

            @Override
            public void onFailure(Throwable caught) {
                GWT.log("Unable to start recording session", caught);
            }

            @Override
            public void onSuccess(RecordingSessionDTO result) {
                sessionStarted = true;
                recordingSession.setId(result.getId());
            }
        });
    }

	private native String getWebId() /*-{
        return $wnd.webId;
    }-*/;


	private native String getDefaultLocale() /*-{
        return $wnd.defaultLocale;
    }-*/;

	private void initializeService() {
        recordingService = GWT.create(IRecordingRemoteService.class);
        ServiceDefTarget recordingServiceEndPoint = (ServiceDefTarget) recordingService;
        recordingServiceEndPoint.setServiceEntryPoint(ServicesDefinition.RECORDING_SERVICE);
	}
	
	@Override
	public void eventRecorded(AbstractGenericEvent event) {
		long currentTime = new Date().getTime();
		event.setDeltaTime((int)(currentTime - this.lastTime));
		this.lastTime = currentTime;

		recorderEvents.add(event);
		logEvents(false);
	}
	
	@Override
	public void stopRecording() {
		super.stopRecording();
		logEvents(true);
	}
	
	protected void logEvents(boolean force) {

		if (recordingLogDTO == null) {
			recordingLogDTO = new RecordingLogDTO();
			recordingLogDTO.setEventTime(new Date());
		}

		if (force || recorderEvents.size() == mode.getBatchSize()) {
			List<AbstractGenericEvent> recorderEventsForPersisting = new ArrayList<AbstractGenericEvent>(recorderEvents);
			recorderEvents.clear();
			logEvents(recorderEventsForPersisting);
		}
	}
	
	protected void logEvents(final List<AbstractGenericEvent> recorderEventsForPersisting) {

		EventsEncoder.EncodedEvent encodedEvents;
		try {
			encodedEvents = eventsEncoder.encodeEvents(recorderEventsForPersisting, blobId);
		} catch (UnsupportedEncodingException e) {
            GWT.log(e.getMessage());
			return;
		}

		recordingLogDTO.setEvent(encodedEvents.data);

		if (recordingSession.getAuditLogs() == null) {
			recordingSession.setAuditLogs(new ArrayList<RecordingLogDTO>());
		}
		recordingSession.getAuditLogs().add(recordingLogDTO);

		if (!sessionStarted) {
            awaitingLogs.add(new AwaitingLog(recordingLogDTO, blobId, encodedEvents.blobs));
		} else {
			if (awaitingLogs.size() > 0) {
				for (AwaitingLog log: awaitingLogs) {
					saveLog(log.log, log.blobId, log.blobs);
				}
				awaitingLogs.clear();
			}
			saveLog(recordingLogDTO, blobId, encodedEvents.blobs);
		}

        blobId += encodedEvents.blobs.size();
		recordingLogDTO = null;
	}

	protected void saveLog(RecordingLogDTO log, long blobId, List<String> blobs) {

        log.setSessionId(this.recordingSession.getId());

        AsyncCallback<Void> asyncCallback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("Unable to save recording log", caught);
            }

            @Override
            public void onSuccess(Void result) {
            }
        };

        if (blobs.size() == 0) {
            recordingService.recordLog(log, asyncCallback);
        } else {
            recordingService.recordLog(log, blobId, blobs, asyncCallback);
        }
	}
}