package sk.seges.acris.player.client.objects.common;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.Scheduler;
import sk.seges.acris.recorder.client.common.Position;
import sk.seges.acris.recorder.client.event.HasAbsolutePosition;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;

public class ObjectAnimation extends Animation {

	private AnimationObject animationObject;
	private EventProperties eventProperties;
	private int duration;
	
	public ObjectAnimation(AnimationObject animationObject, EventProperties eventProperties, int duration) {
		this.eventProperties = eventProperties;
		this.duration = duration;
		this.animationObject = animationObject;
	}

	public void run() {
		super.run(duration);
	}

	@Override
	protected void onUpdate(double progress) {

		AbstractGenericEvent event = eventProperties.getEvent();

		if (event instanceof HasAbsolutePosition) {

            HasAbsolutePosition positionEvent = (HasAbsolutePosition)event;

			Position position = new Position(positionEvent.getAbsoluteClientX(), positionEvent.getAbsoluteClientY());

			final int currentPositionX = (int)((position.left - animationObject.getObjectPositionX()) * progress) + animationObject.getObjectPositionX();
			final int currentPositionY = (int)((position.top - animationObject.getObjectPositionY()) * progress) + animationObject.getObjectPositionY();

            animationObject.setPosition(currentPositionX, currentPositionY);
		}
	}
}