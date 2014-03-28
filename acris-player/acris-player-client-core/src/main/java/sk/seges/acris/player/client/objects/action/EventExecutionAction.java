package sk.seges.acris.player.client.objects.action;

import sk.seges.acris.player.client.objects.EventExecutor;
import sk.seges.acris.player.client.objects.common.AnimationObject;
import sk.seges.acris.player.client.objects.common.EventProperties;
import sk.seges.acris.player.client.objects.common.ObjectAnimation;

public interface EventExecutionAction {

	ObjectAnimation run(EventProperties cursorProperties);

}
