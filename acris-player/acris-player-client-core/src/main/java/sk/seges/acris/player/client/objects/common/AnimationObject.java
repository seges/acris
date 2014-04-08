package sk.seges.acris.player.client.objects.common;

import com.google.gwt.dom.client.Document;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import sk.seges.acris.player.client.listener.CompleteHandler;
import sk.seges.acris.player.client.objects.action.MouseMoveAction;
import sk.seges.acris.recorder.client.event.MouseEvent;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;
import sk.seges.acris.recorder.client.tools.CacheMap;

public class AnimationObject implements EventMirror {

	protected static final int DEFAULT_OBJECT_SPEED = 100; //pixels per second

	public static final int REALTIME_OBJECT_SPEED = 300;
	public static final int PRESENTATION_OBJECT_SPEED = 150;

	protected final int speed;
	private final CacheMap cacheMap;

	private int objectPositionX;
	private int objectPositionY;

	private final AcceptsOneWidget widget;

	public AnimationObject(int speed, CacheMap cacheMap) {
		this(new SimplePanel(), speed, cacheMap);
	}

	public AnimationObject(AcceptsOneWidget widget, int speed, CacheMap cacheMap) {
		this.widget = widget;
		this.cacheMap = cacheMap;
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

	public static class Position {

		public int top;

		public int left;

		public Position(int left, int top) {
			this.left = left;
			this.top = top;
		}
	}

	protected void setWidget(Widget w) {
		widget.setWidget(w);
	}

	public void setPosition(int x, int y) {
		this.objectPositionX = x;
		this.objectPositionY = y;
		DOM.setStyleAttribute(getWidget().getElement(), "left", x + "px");
		DOM.setStyleAttribute(getWidget().getElement(), "top", y + "px");
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

			MouseEvent mouseEvent = (MouseEvent)cursorProperties.getEvent();

			cursorProperties.getEvent().fireEvent();
			switch (cursorProperties.getEvent().getTypeInt()) {
				case MouseEvent.MOUSE_MOVE_TYPE:
					new MouseMoveAction().createAnimation(cursorProperties, completeHandler).run();
					break;
				case MouseEvent.MOUSE_CLICK_TYPE:
					GQuery.$(mouseEvent.getElement()).click();
					completeHandler.onComplete();
					break;
				case MouseEvent.MOUSE_DOUBLE_CLICK_TYPE:
					GQuery.$(mouseEvent.getElement()).dblclick();
					completeHandler.onComplete();
					break;
				case MouseEvent.MOUSE_WHEEL_TYPE:
					int scrollTop1 = Document.get().getScrollTop();
					GQuery.$(GQuery.window).scrollTo(mouseEvent.getClientX(), mouseEvent.getClientY() + scrollTop1);
					completeHandler.onComplete();
					break;
				default:
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