package sk.seges.acris.player.client.objects.common;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.player.client.event.AnimationEvent;

import com.google.gwt.animation.client.Animation;
import sk.seges.acris.recorder.client.event.MouseEvent;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;

public class ObjectAnimation extends Animation {

	private AnimationObject animationObject;
	private EventProperties eventProperties;
	private List<AnimationEvent> events = new ArrayList<AnimationEvent>();
	private int duration;
	
	public ObjectAnimation(AnimationObject animationObject, EventProperties eventProperties, int duration) {
		this.eventProperties = eventProperties;
		this.duration = duration;
		this.animationObject = animationObject;
	}

	public int getDuration() {
		return duration;
	}
	
	public void addAnimationEvent(AnimationEvent event) {
		events.add(event);
	}

	public void removeAnimationEvent(AnimationEvent event) {
		events.remove(event);
	}

	public void clearAnimationEvents() {
		events.clear();
	}

	@Override
	protected void onUpdate(double progress) {

		AbstractGenericEvent fakeEvent = eventProperties.getEvent();

		if (fakeEvent instanceof MouseEvent) {

			MouseEvent mouseEvent = (MouseEvent)fakeEvent;

			int currentPositionX = (int)((mouseEvent.getClientX() - animationObject.getObjectPositionX()) * progress) + animationObject.getObjectPositionX();
			int currentPositionY = (int)((mouseEvent.getClientY() - animationObject.getObjectPositionY()) * progress) + animationObject.getObjectPositionY();

			animationObject.setPosition(currentPositionX, currentPositionY);
		}
	}
	
	@Override
	protected void onComplete() {
		super.onComplete();
		
		for (AnimationEvent event : events) {
			event.onComplete();
		}
	}
}