package sk.seges.acris.player.client.objects.action;

import sk.seges.acris.player.client.objects.common.AnimationObject;
import sk.seges.acris.player.client.objects.common.EventMirror;
import sk.seges.acris.player.client.objects.common.EventProperties;
import sk.seges.acris.player.client.objects.common.ObjectAnimation;
import sk.seges.acris.player.client.objects.utils.PositionUtils;

public class MouseMoveAction implements EventExecutionAction {

	public ObjectAnimation run(EventProperties cursorProperties) {

		EventMirror eventMirror = cursorProperties.getEventMirror();
		AnimationObject animationObject = (AnimationObject) eventMirror;

//		animationObject.setObjectPositionX().setObjectPositionX(animationObject.getPositionX());
//		eventExecutor.setObjectPositionY(animationObject.getPositionY());
//
//		if (cursorProperties.getTargetElementXpath() != null) {
//			AnimationObject.Position position = animationObject.getElementAbsolutePosition(animationObject.getElement(cursorProperties.getTargetElementXpath()), true);
//			cursorProperties.setTargetPositionX(position.left);
//			cursorProperties.setTargetPositionY(position.top);
//		}

		return new ObjectAnimation(animationObject, cursorProperties, PositionUtils.calculateDuration(cursorProperties, animationObject.getSpeed()));
	}
}
