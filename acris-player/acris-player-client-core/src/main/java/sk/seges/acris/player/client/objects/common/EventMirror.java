package sk.seges.acris.player.client.objects.common;

import com.google.gwt.user.client.ui.IsWidget;
import sk.seges.acris.player.client.listener.CompleteHandler;

public interface EventMirror {

	void initialize();

	void runAction(EventProperties cursorProperties, CompleteHandler completeHandler);

	IsWidget getWidget();

	void destroy();
}
