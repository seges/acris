package sk.seges.acris.recorder.client.recorder.support;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import sk.seges.acris.callbacks.client.TrackingAsyncCallback;
import sk.seges.acris.recorder.client.listener.RecorderListener;
import sk.seges.acris.recorder.rpc.event.encoding.EventEncoder;
import sk.seges.acris.recorder.rpc.event.generic.AbstractGenericEvent;
import sk.seges.acris.recorder.rpc.event.generic.AbstractGenericTargetableEvent;
import sk.seges.acris.recorder.rpc.service.IAuditTrailService;
import sk.seges.acris.recorder.rpc.service.IAuditTrailServiceAsync;
import sk.seges.acris.recorder.rpc.transfer.StringMapper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

abstract public class Recorder extends AbstractRecorder implements RecorderListener {
	
	protected final RecorderMode mode;
	
	protected final List<AbstractGenericEvent> recorderEvents = new ArrayList<AbstractGenericEvent>();
	protected final StringMapper targetsList = new StringMapper();
	private IAuditTrailServiceAsync auditTrailService;

	protected Recorder(RecorderMode mode) {
		super();
		this.mode = mode;
		addRecordListener(this);
		initializeService();
	}
	
	private void initializeService() {
        String auditTrailServiceURL = GWT.getModuleBaseURL() + "acris-service/logservice";
        auditTrailService = GWT.create(IAuditTrailService.class);
        //TODO Initiaze session also
        ServiceDefTarget auditTrailServiceEndPoint = (ServiceDefTarget)auditTrailService;
        auditTrailServiceEndPoint.setServiceEntryPoint(auditTrailServiceURL);
	}
	
	@Override
	public void eventRecorded(AbstractGenericEvent event) {
		recorderEvents.add(event);
		if (event instanceof AbstractGenericTargetableEvent) {
			targetsList.add(((AbstractGenericTargetableEvent)event).getRelatedTargetId());
		}
		
		logEvents();
	}
	
	@Override
	public void stopRecording() {
		super.stopRecording();
		logEvents();
	}
	
	protected void logEvents() {
		if (recorderEvents.size() == mode.getBatchSize()) {
			
			List<AbstractGenericEvent> recorderEventsForPersisting = new ArrayList<AbstractGenericEvent>();
			StringMapper targetsListForPersisting = new StringMapper();

			for (AbstractGenericEvent age : recorderEvents) {
				recorderEventsForPersisting.add(age);
			}
			
			recorderEvents.clear();
			
			for (String target : targetsList) {
				targetsListForPersisting.add(target);
			}
			
			targetsList.clear();
			
			logEvents(recorderEventsForPersisting, targetsListForPersisting);
		}
	}
	
	protected void logEvents(final List<AbstractGenericEvent> recorderEventsForPersisting, StringMapper targetsListForPersisting) {

		final int[] encodedEvents = encodeEvents(recorderEventsForPersisting, targetsListForPersisting);

		TrackingAsyncCallback<Void> callback = new TrackingAsyncCallback<Void>() {

			public void onFailureCallback(Throwable caught) {
				GWT.log("Exception occured while logging events", caught);
			}

			public void onSuccessCallback(Void result) {
				GWT.log("Events logged", null);
				
//				StringMapper resultStringMapper = new StringMapper();

//				auditTrailService.getAuditLogs(resultStringMapper, new AsyncCallback<int[]>() {
//
//					@Override
//					public void onFailure(Throwable caught) {
//					}
//
//					@Override
//					public void onSuccess(int[] result) {
//						if (result.length != encodedEvents.length) {
//							GWT.log("Fail ... length", null);
//						}
//						
//						for (int i  = 0; i < result.length; i++) {
//							if (result[i] != encodedEvents[i]) {
//								GWT.log("Fail ... " + i, null);
//							}
//						}
//					}
//				});
			}
			
		};
		
		String[] targets = new String[targetsListForPersisting.size()];
		    
		int i = 0;
		for (String target : targetsListForPersisting) {
			targets[i++] = target;
		}
		
		if (encodedEvents.length == 1) {
			if (targetsListForPersisting.size() == 1) {
				auditTrailService.logUserActivity(encodedEvents[0], callback);
			} else {
				auditTrailService.logUserActivity(encodedEvents[0], targetsListForPersisting.get(1), callback);
			}
		} else {
			auditTrailService.logUserActivity(encodedEvents, targets, callback);
		}
				


	}
	
	private int[] encodeEvents(List<AbstractGenericEvent> recorderEventsForPersisting, StringMapper targetsListForPersisting) {
		List<Integer> encodedEvents = new LinkedList<Integer>();
		
		for (AbstractGenericEvent event : recorderEventsForPersisting) {
			int[] encodedEvent = EventEncoder.encodeEvent(event, targetsListForPersisting);
			
			for (int e : encodedEvent) {
				encodedEvents.add(e);
			}
		}

		int[] events = new int[encodedEvents.size()];
		
		int i = 0;
		for (Integer eventPart : encodedEvents) {
			events[i++] = eventPart;
		}
		return events;
	}
}