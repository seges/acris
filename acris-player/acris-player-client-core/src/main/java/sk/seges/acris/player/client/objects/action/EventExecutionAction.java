package sk.seges.acris.player.client.objects.action;

import sk.seges.acris.player.client.listener.CompleteHandler;
import sk.seges.acris.player.client.objects.common.EventProperties;
import sk.seges.acris.player.client.objects.common.ObjectAnimation;

public interface EventExecutionAction {

	ObjectAnimation createAnimation(EventProperties cursorProperties, CompleteHandler completeHandler);

}
