package sk.seges.acris.player.client.objects.common;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import sk.seges.acris.player.client.listener.CompleteHandler;
import sk.seges.acris.player.client.objects.action.MouseMoveAction;
import sk.seges.acris.recorder.client.event.MouseEvent;

public class AnimationObject implements EventMirror, HasPosition {

	protected static final int DEFAULT_OBJECT_SPEED = 100; //pixels per second

	public static final int REALTIME_OBJECT_SPEED = 300;
	public static final int PRESENTATION_OBJECT_SPEED = 150;
    public static final int NO_SPEED = 0;

	private int objectPositionX;
	private int objectPositionY;

    private int speed;

	private final AcceptsOneWidget widget;

	public AnimationObject(int speed) {
		this(new SimplePanel(), speed);
	}

	public AnimationObject(AcceptsOneWidget widget, int speed) {
		this.widget = widget;
		this.speed = speed;
	}

	@Override
	public Widget getWidget() {
		return (Widget) widget;
	}

	public int getObjectPositionX() {
		return objectPositionX;
	}

	public int getObjectPositionY() {
		return objectPositionY;
	}

	public int getSpeed()  {
		return speed;
	}

    protected void setWidget(Widget w) {
		widget.setWidget(w);
	}

	public void setPosition(int x, int y) {
        if (x != this.objectPositionX) {
            this.objectPositionX = x;
            DOM.setStyleAttribute(getWidget().getElement(), "left", x + "px");
        }

        if (y != this.objectPositionY) {
            this.objectPositionY = y;
            DOM.setStyleAttribute(getWidget().getElement(), "top", y + "px");
        }
	}

    public void setSpeed(int speed) {
        this.speed = speed;
    }

	public void destroy() {
		hide();
	}
	
	public void initialize() {
		DOM.setStyleAttribute(getWidget().getElement(), "visibility", "visible");
	}

	@Override
	public void runAction(EventProperties cursorProperties, final CompleteHandler completeHandler) {
		if (cursorProperties.getEvent() != null) {

			if (!(cursorProperties.getEvent() instanceof MouseEvent)) {
				completeHandler.onComplete();
				return;
			}

			switch (cursorProperties.getEvent().getTypeInt()) {
				case MouseEvent.MOUSE_MOVE_TYPE:
                case MouseEvent.MOUSE_OUT_TYPE:
                case MouseEvent.MOUSE_OVER_TYPE:
					new MouseMoveAction().createAnimation(cursorProperties, completeHandler).run();
					break;
				default:
                    cursorProperties.getEvent().fireEvent();
					completeHandler.onComplete();
					break;
			}
		} else {
            completeHandler.onComplete();
        }
	}

	private void hide() {
		DOM.setStyleAttribute(getWidget().getElement(), "visibility", "hidden");
	}
}