package sk.seges.acris.scheduler.client;

import sk.seges.acris.callbacks.client.TrackingAsyncCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.impl.SchedulerImpl;

public class TrackingSchedulerImpl extends SchedulerImpl {

	public static final TrackingSchedulerImpl INSTANCE = GWT.create(TrackingSchedulerImpl.class);
	
	private class EmptyAsyncCallback extends TrackingAsyncCallback<Void> {

		private final TrackingScheduledCommand cmd;

		public EmptyAsyncCallback(String name, TrackingScheduledCommand cmd) {
			this.cmd = cmd;
			start(name);
		}
		
		@Override
		public void onSuccessCallback(Void arg0) {
			cmd.run();
		}

		@Override
		public void onFailureCallback(Throwable cause) {}
		
	}

	@Override
	public void scheduleDeferred(ScheduledCommand cmd) {
		if (cmd instanceof TrackingScheduledCommand) {
			scheduleDeferred((TrackingScheduledCommand) cmd, null);
		} else {
			super.scheduleDeferred(cmd);
		}
	}
	
	public void scheduleDeferred(TrackingScheduledCommand cmd, String name) {

		//Emulate asynchronous nation of the processing. Otherwise offline generator will
		//generate offline before scripts are fully loaded
		cmd.setCallback(new EmptyAsyncCallback(name, cmd));
		super.scheduleDeferred(cmd);
	}
}