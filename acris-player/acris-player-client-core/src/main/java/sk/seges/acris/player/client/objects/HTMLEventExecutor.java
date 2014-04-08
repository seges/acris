package sk.seges.acris.player.client.objects;

import sk.seges.acris.player.client.listener.CompleteHandler;
import sk.seges.acris.player.client.objects.common.EventProperties;
import sk.seges.acris.recorder.client.event.HtmlEvent;

public class HTMLEventExecutor extends EventExecutor {

	@Override
	public void runAction(EventProperties cursorProperties, CompleteHandler completeHandler) {
		if (!(cursorProperties.getEvent() instanceof HtmlEvent)) {
			completeHandler.onComplete();
			return;
		}
		super.runAction(cursorProperties, completeHandler);
	}
}
