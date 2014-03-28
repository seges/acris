package sk.seges.acris.player.client.objects.common;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import sk.seges.acris.player.client.event.AnimationEvent;
import sk.seges.acris.player.client.listener.CompleteHandler;
import sk.seges.acris.player.client.objects.action.MouseMoveAction;
import sk.seges.acris.recorder.client.event.MouseEvent;
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

	public AnimationObject(CacheMap cacheMap) {
		this(new SimplePanel(), DEFAULT_OBJECT_SPEED, cacheMap);
	}

	public AnimationObject(int speed, CacheMap cacheMap) {
		this(new SimplePanel(), speed, cacheMap);
	}

	public AnimationObject(AcceptsOneWidget widget, CacheMap cacheMap) {
		this(widget, DEFAULT_OBJECT_SPEED, cacheMap);
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

	public void setObjectPositionX(int objectPositionX) {
		this.objectPositionX = objectPositionX;
	}

	public int getObjectPositionY() {
		return objectPositionY;
	}

	public void setObjectPositionY(int objectPositionY) {
		this.objectPositionY = objectPositionY;
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
		
		public Position(String stringPosition) {
			int index = stringPosition.indexOf(",");
			
			if (index == -1) {
				throw new IllegalArgumentException("Invalid position representation!");
			}
			
			String left = stringPosition.substring(index).trim();
			String top = stringPosition.substring(index + 1).trim();
			
			this.left = Integer.valueOf(left).intValue();
			this.top = Integer.valueOf(top).intValue();
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

//		if (objectAnimation != null) {
//			objectAnimation.cancel();
//		}
//		if (timer != null) {
//			timer.cancel();
//		}
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
					run(new MouseMoveAction().run(cursorProperties), completeHandler);
					break;
				default:
					completeHandler.onComplete();
			}
		} else {
			Timer timer = new Timer() {

				public void run() {
					completeHandler.onComplete();
				}
			};
			timer.schedule(cursorProperties.getWaitTime());
		}
	}

	private void run(ObjectAnimation objectAnimation, final CompleteHandler completeHandler) {
		objectAnimation.run(objectAnimation.getDuration());
		if (objectAnimation.getDuration() > 0 ) {
			objectAnimation.addAnimationEvent(new AnimationEvent() {

				public void onComplete() {
					completeHandler.onComplete();
				}
			});
		} else {
			completeHandler.onComplete();
		}
	}

	private void hide() {
		DOM.setStyleAttribute(getWidget().getElement(), "visibility", "hidden");
	}

	public Element getElement(String xpath) {
		return cacheMap.resolveElement(xpath);
	}

	public Position getElementAbsolutePosition(Element element, boolean offset) {
		int x = DOM.getAbsoluteLeft(element);
		int y = DOM.getAbsoluteTop(element);
		
		if (offset) {
			x += element.getClientWidth() / 2;
			y += element.getClientHeight() / 2;
		}
		return new Position(x, y);
	}


//	public void wait(int time) {
//		EventProperties cursorProperties = new EventProperties(this);
//		cursorProperties.setWaitTime(time);
//		getActionsQueue().add(cursorProperties);
//		runAction();
//	}




}
