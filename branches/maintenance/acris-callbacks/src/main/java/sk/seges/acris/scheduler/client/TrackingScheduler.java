package sk.seges.acris.scheduler.client;

import com.google.gwt.core.client.Scheduler;

public abstract class TrackingScheduler extends Scheduler {

	public static Scheduler get() {
		return TrackingSchedulerImpl.INSTANCE;
	}

}