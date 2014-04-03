package sk.seges.acris.player.client.objects.action;

import sk.seges.acris.player.client.listener.CompleteHandler;
import sk.seges.acris.player.client.objects.common.AnimationObject;
import sk.seges.acris.player.client.objects.common.EventMirror;
import sk.seges.acris.player.client.objects.common.EventProperties;
import sk.seges.acris.player.client.objects.common.ObjectAnimation;
import sk.seges.acris.player.client.objects.utils.PositionUtils;

public class MouseMoveAction implements EventExecutionAction {

	public ObjectAnimation createAnimation(EventProperties cursorProperties, final CompleteHandler completeHandler) {

		EventMirror eventMirror = cursorProperties.getEventMirror();
		AnimationObject animationObject = (AnimationObject) eventMirror;

		return new ObjectAnimation(animationObject, cursorProperties, PositionUtils.calculateDuration(cursorProperties, animationObject.getSpeed())) {
			@Override
			protected void onComplete() {
				super.onComplete();
				completeHandler.onComplete();
			}
		};
	}
}
