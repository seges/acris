package sk.seges.acris.scheduler.client;

import java.util.Date;

import sk.seges.acris.callbacks.client.TrackingAsyncCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.impl.SchedulerImpl;

public class TrackingSchedulerImpl extends SchedulerImpl {

	public static final TrackingSchedulerImpl INSTANCE = GWT.create(TrackingSchedulerImpl.class);
	
	private class EmptyAsyncCallback extends TrackingAsyncCallback<Void> {

		private final TrackingScheduledCommand cmd;

		public EmptyAsyncCallback(TrackingScheduledCommand cmd) {
			this.cmd = cmd;
			setRequestId(new Date().getTime());
		}
		
		@Override
		public void onSuccessCallback(Void arg0) {
			cmd.run();
		}

		@Override
		public void onFailureCallback(Throwable cause) {}
		
	}

	public void scheduleDeferred(TrackingScheduledCommand cmd) {

		//Emulate asynchronous nation of the processing. Otherwise offline generator will
		//generate offline before scripts are fully loaded
		cmd.setCallback(new EmptyAsyncCallback(cmd));
		super.scheduleDeferred(cmd);
	}
}