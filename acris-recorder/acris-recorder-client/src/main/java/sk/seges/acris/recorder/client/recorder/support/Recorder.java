package sk.seges.acris.recorder.client.recorder.support;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import sk.seges.acris.recorder.client.event.encoding.EventEncoder;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericTargetableEvent;
import sk.seges.acris.recorder.client.listener.RecorderListener;
import sk.seges.acris.recorder.client.session.RecordingSessionProvider;
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
	private IRecordingRemoteServiceAsync recordingService;

	private RecordingSessionDTO recordingSession;
	private RecordingLogDTO recordingLogDTO;

	private boolean sessionStarted = false;
	private List<RecordingLogDTO> awaitingLogs = new ArrayList<RecordingLogDTO>();

	protected Recorder(RecorderMode mode) {
		super();
		this.mode = mode;

		this.recordingSession = new RecordingSessionDTO();

		GWT.log("Starting up...");

		RecordingSessionProvider.getSession(new AsyncCallback<RecordingSessionDetailParams>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(RecordingSessionDetailParams result) {
				recordingSession.setSessionInfo(result.toString());
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
		});

		recordingSession.setSessionTime(new Date());
		recordingSession.setLanguage(getDefaultLocale());
		recordingSession.setWebId(getWebId());

		addRecordListener(this);
		initializeService();
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
			recordingLogDTO.setSession(this.recordingSession);
		}

		if (force || recorderEvents.size() == mode.getBatchSize()) {
			List<AbstractGenericEvent> recorderEventsForPersisting = new ArrayList<AbstractGenericEvent>(recorderEvents);
			recorderEvents.clear();
			logEvents(recorderEventsForPersisting);
		}
	}
	
	protected void logEvents(final List<AbstractGenericEvent> recorderEventsForPersisting) {

		String encodedEvents;
		try {
			encodedEvents = encodeEvents(recorderEventsForPersisting);
		} catch (UnsupportedEncodingException e) {
			GWT.log("Unable to encode events", e);
			return;
		}

		recordingLogDTO.setEvent(encodedEvents.replace('\0', ' '));

		if (recordingSession.getAuditLogs() == null) {
			recordingSession.setAuditLogs(new ArrayList<RecordingLogDTO>());
		}
		recordingSession.getAuditLogs().add(recordingLogDTO);

		if (!sessionStarted) {
			awaitingLogs.add(recordingLogDTO);
		} else {
			if (awaitingLogs.size() > 0) {
				for (RecordingLogDTO log: awaitingLogs) {
					saveLog(log);
				}
				awaitingLogs.clear();
			}
			saveLog(recordingLogDTO);
		}

		recordingLogDTO = null;
	}

	private void saveLog(RecordingLogDTO log) {
		recordingService.recordLog(log, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Unable to save recording log", caught);
			}

			@Override
			public void onSuccess(Void result) {
			}
		});
	}

	private static final String DELIMITER = "|";

	private String encodeEvents(List<AbstractGenericEvent> recorderEventsForPersisting) throws UnsupportedEncodingException {

		GWT.log("Encoding events...");

		String result = "";

		for (AbstractGenericEvent event : recorderEventsForPersisting) {
			byte[] encodedEvent = EventEncoder.encodeEvent(event);

			result += new String(encodedEvent, "Unicode") + DELIMITER;

			if (event instanceof AbstractGenericTargetableEvent) {
				result += ((AbstractGenericTargetableEvent)event).getRelatedTargetId();
			}

			result += DELIMITER;
		}

		return result;
	}
}