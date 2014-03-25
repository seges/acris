package sk.seges.acris.player.client.objects;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.player.client.event.AnimationEvent;

import com.google.gwt.animation.client.Animation;

class ObjectAnimation extends Animation {

	private AnimationObject animationObject;
	private AnimationProperties animationProperties;
	private List<AnimationEvent> events = new ArrayList<AnimationEvent>();
	
	public ObjectAnimation(AnimationObject animationObject, AnimationProperties animationProperties) {
		this.animationProperties = animationProperties;
		this.animationObject = animationObject;
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
		int currentPositionX = (int)((animationProperties.getTargetPositionX() - animationProperties.getObjectPositionX()) * progress) + 
																	animationProperties.getObjectPositionX();
		int currentPositionY = (int)((animationProperties.getTargetPositionY() - animationProperties.getObjectPositionY()) * progress) + 
																	animationProperties.getObjectPositionY();
		
		animationObject.setPosition(currentPositionX, currentPositionY);
	}
	
	@Override
	protected void onComplete() {
		super.onComplete();
		
		for (AnimationEvent event : events) {
			event.onComplete();
		}
	}
}