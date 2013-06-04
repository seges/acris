package sk.seges.acris.scheduler.client;

import sk.seges.acris.callbacks.client.TrackingAsyncCallback;

import com.google.gwt.core.client.Scheduler.ScheduledCommand;

public abstract class TrackingScheduledCommand implements ScheduledCommand {

	private TrackingAsyncCallback<?> callback;
	
	@Override
	public void execute() {
		if (callback != null) {
			callback.onSuccess(null);
		} else {
			run();
		}
	}
	
	public abstract void run();
	
	void setCallback(TrackingAsyncCallback<?> callback) {
		this.callback = callback;
	}
}