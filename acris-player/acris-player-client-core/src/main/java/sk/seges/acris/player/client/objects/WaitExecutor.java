package sk.seges.acris.player.client.objects;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.IsWidget;
import sk.seges.acris.player.client.listener.CompleteHandler;
import sk.seges.acris.player.client.objects.common.EventMirror;
import sk.seges.acris.player.client.objects.common.EventProperties;

public class WaitExecutor implements EventMirror {

    private int speed;

    @Override
	public void initialize() {}

	@Override
	public void runAction(EventProperties cursorProperties, final CompleteHandler completeHandler) {
		if (cursorProperties.getEvent() == null || cursorProperties.getEvent().getDeltaTime() <= 0) {
			completeHandler.onComplete();
			return;
		}

		Timer timer = new Timer() {

			public void run() {
				completeHandler.onComplete();
			}
		};
		timer.schedule(cursorProperties.getEvent().getDeltaTime());
	}

	@Override
	public IsWidget getWidget() {
		return null;
	}

	@Override
	public void destroy() {}

    @Override
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public int getSpeed() {
        return speed;
    }
}