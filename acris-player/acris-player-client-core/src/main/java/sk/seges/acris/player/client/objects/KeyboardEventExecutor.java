package sk.seges.acris.player.client.objects;

import sk.seges.acris.player.client.listener.CompleteHandler;
import sk.seges.acris.player.client.objects.common.EventProperties;
import sk.seges.acris.recorder.client.event.HtmlEvent;
import sk.seges.acris.recorder.client.event.KeyboardEvent;

public class KeyboardEventExecutor extends EventExecutor {

	@Override
	public void runAction(EventProperties cursorProperties, CompleteHandler completeHandler) {
		if (!(cursorProperties.getEvent() instanceof KeyboardEvent)) {
			super.runAction(cursorProperties, completeHandler);
			return;
		}
	}
}
