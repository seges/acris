package sk.seges.acris.player.client.objects;

import com.google.gwt.user.client.ui.IsWidget;
import sk.seges.acris.player.client.listener.CompleteHandler;
import sk.seges.acris.player.client.objects.common.EventMirror;
import sk.seges.acris.player.client.objects.common.EventProperties;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;

public class EventExecutor implements EventMirror {

    protected int speed;

    @Override
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
	public void initialize() {}

	@Override
	public void runAction(EventProperties cursorProperties, CompleteHandler completeHandler) {
		AbstractGenericEvent event = cursorProperties.getEvent();
		if (event != null) {
			event.fireEvent();
		}
		completeHandler.onComplete();
	}

	@Override
	public IsWidget getWidget() {
		return null;
	}

	@Override
	public void destroy() {}
}
