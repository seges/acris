package sk.seges.acris.player.client.objects.common;

import com.google.gwt.animation.client.Animation;
import sk.seges.acris.recorder.client.event.MouseEvent;
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

		if (event instanceof MouseEvent) {

			MouseEvent mouseEvent = (MouseEvent)event;

			AnimationObject.Position position;

//			if (mouseEvent.getRelatedTargetXpath() != null) {
//				position = animationObject.getElementAbsolutePosition(animationObject.getElement(mouseEvent.getRelatedTargetXpath()), true);
//			} else {
				position = new AnimationObject.Position(mouseEvent.getClientX(), mouseEvent.getClientY());
//			}

			int currentPositionX = (int)((position.left - animationObject.getObjectPositionX()) * progress) + animationObject.getObjectPositionX();
			int currentPositionY = (int)((position.top - animationObject.getObjectPositionY()) * progress) + animationObject.getObjectPositionY();

			animationObject.setPosition(currentPositionX, currentPositionY);
		}
	}
}