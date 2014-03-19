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
import sk.seges.acris.recorder.shared.service.IRecordingService;
import sk.seges.acris.recorder.shared.service.IRecordingServiceAsync;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

abstract public class Recorder extends AbstractRecorder implements RecorderListener {
	
	protected final RecorderMode mode;
	
	protected final List<AbstractGenericEvent> recorderEvents = new ArrayList<AbstractGenericEvent>();
	private IRecordingServiceAsync auditTrailService;

	private RecordingSessionDTO recordingSession;
	private RecordingLogDTO recordingLogDTO;

	protected Recorder(RecorderMode mode) {
		super();
		this.mode = mode;

		this.recordingSession = new RecordingSessionDTO();

		RecordingSessionProvider.getSession(new AsyncCallback<RecordingSessionDetailParams>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(RecordingSessionDetailParams result) {
				recordingSession.setSessionInfo(result.toString());
			}
		});

		recordingSession.setLanguage(getDefaultLocale());
		recordingSession.setWebId(getWebId());

		addRecordListener(this);
		initializeService();
	}

	private native final String getWebId() /*-{
        return $wnd.webId;
    }-*/;


	private native final String getDefaultLocale() /*-{
        return $wnd.defaultLocale;
    }-*/;

	private void initializeService() {
        String auditTrailServiceURL = GWT.getModuleBaseURL() + "acris-service/logservice";
        auditTrailService = GWT.create(IRecordingService.class);
        //TODO Initiaze session also
        ServiceDefTarget auditTrailServiceEndPoint = (ServiceDefTarget)auditTrailService;
        auditTrailServiceEndPoint.setServiceEntryPoint(auditTrailServiceURL);
	}
	
	@Override
	public void eventRecorded(AbstractGenericEvent event) {
		recorderEvents.add(event);
		logEvents();
	}
	
	@Override
	public void stopRecording() {
		super.stopRecording();
		logEvents();
	}
	
	protected void logEvents() {

		if (recordingLogDTO == null) {
			recordingLogDTO = new RecordingLogDTO();
			recordingLogDTO.setEventTime(new Date());
			recordingLogDTO.setSession(this.recordingSession);
		}

		if (recorderEvents.size() == mode.getBatchSize()) {
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

		recordingLogDTO.setEvent(encodedEvents);
		auditTrailService.recordLog(recordingLogDTO, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Void result) {

			}
		});
		recordingLogDTO = null;
	}

	private static final String DELIMITER = "|";

	private String encodeEvents(List<AbstractGenericEvent> recorderEventsForPersisting) throws UnsupportedEncodingException {

		String result = "";

		for (AbstractGenericEvent event : recorderEventsForPersisting) {
			byte[] encodedEvent = EventEncoder.encodeEvent(event);

			result += new String(encodedEvent, "UTF-8") + DELIMITER;

			if (event instanceof AbstractGenericTargetableEvent) {
				result += ((AbstractGenericTargetableEvent)event).getRelatedTargetId();
			}

			result += DELIMITER;
		}

		return result;
	}
}