package sk.seges.acris.player.client.objects;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetHandlerProvider;
import sk.seges.acris.player.client.event.AnimationEvent;
import sk.seges.acris.player.client.event.handler.StateClickHandler;
import sk.seges.acris.player.client.event.handler.StateClickHandler;
import sk.seges.acris.recorder.client.event.MouseEvent;
import sk.seges.acris.recorder.client.tools.CacheMap;

import java.util.LinkedList;
import java.util.List;

public class AnimationObject extends Composite {

	private List<AnimationProperties> actionsQueue = new LinkedList<AnimationProperties>();
	private List<AnimationObject> synchronizedObjects = new LinkedList<AnimationObject>();
	private ObjectWrapper<Boolean> running = new ObjectWrapper<Boolean>();

	private ObjectAnimation objectAnimation;
	private Timer timer;
	
	private boolean isAttached = false;
	protected static final int DEFAULT_OBJECT_SPEED = 100; //pixels per second

	public static final int REALTIME_OBJECT_SPEED = 300;
	public static final int PRESENTATION_OBJECT_SPEED = 150;

	protected final int speed;
	private final CacheMap cacheMap;

	public AnimationObject(CacheMap cacheMap) {
		this(DOM.createDiv(), DEFAULT_OBJECT_SPEED, cacheMap);
	}

	public AnimationObject(int speed, CacheMap cacheMap) {
		this(DOM.createDiv(), speed, cacheMap);
	}

	public AnimationObject(Element e, CacheMap cacheMap) {
		this(e, DEFAULT_OBJECT_SPEED, cacheMap);
	}

	public AnimationObject(Element e, int speed, CacheMap cacheMap) {
		if (e != null) {
			setElement(e);
		}
		running.value = false;
		this.cacheMap = cacheMap;
		this.speed = speed;
	}

	public static double calculateDistance(AnimationProperties properties) {
		return calculateDistance(properties.getObjectPositionX(), properties.getObjectPositionY(), 
			properties.getTargetPositionX(), properties.getTargetPositionY());
	}
	
	public static double calculateDistance(int x, int y, int tx, int ty) {
		double dy = ty - y;
		double dx = tx - x;
		
		return Math.sqrt(dy * dy + dx * dx);
	}
	
	private int calculateDuration(AnimationProperties properties) {
		double time = calculateDistance(properties)/speed;
		time *= 1000;
		return (int)(time+0.5);
	}
	
	public void synchronizeWith(AnimationObject animationObject) {
		this.actionsQueue = animationObject.getActionsQueue();
		this.running = animationObject.getRunningProperty();
		synchronizedObjects.add(animationObject);
	}
	
	List<AnimationProperties> getActionsQueue() {
		return actionsQueue;
	}
	
	ObjectWrapper<Boolean> getRunningProperty() {
		return running;
	}
	
	class ObjectWrapper<T> {
		public T value;
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

		if (w != null) {
			w.removeFromParent();
			DOM.appendChild(getElement(), w.getElement());
		}
	}

	public boolean isAttached() {
		return isAttached;
	}

	protected void onAttach() {
		DOM.setEventListener(getElement(), this);
		isAttached = true;
		onLoad();
	}

	protected void onDetach() {
		onUnload();
		DOM.setEventListener(getElement(), null);
		isAttached = false;
	}

	public void setPosition(int x, int y) {
		DOM.setStyleAttribute(getElement(), "left", x + "px");
		DOM.setStyleAttribute(getElement(), "top", y + "px");
	}

	private int getPositionX() {
		String xPosition = DOM.getStyleAttribute(getElement(), "left")
				.replaceAll("px", "").trim();
		return Integer.parseInt(xPosition);
	}

	private int getPositionY() {
		String yPosition = DOM.getStyleAttribute(getElement(), "top")
				.replaceAll("px", "").trim();
		return Integer.parseInt(yPosition);
	}

	public void stop() {
		hide();
		
		running.value = false;
		
		if (objectAnimation != null) {
			objectAnimation.cancel();
		}
		if (timer != null) {
			timer.cancel();
		}
	}
	
	public void start() {
		show();
		actionsQueue.clear();
		running.value = false;
	}
	
	private void show() {
		DOM.setStyleAttribute(getElement(), "visibility", "visible");
	}
	
	private void hide() {
		DOM.setStyleAttribute(getElement(), "visibility", "hidden");
	}

	public void move(String elementXpath) {
		AnimationProperties cursorProperties = new AnimationProperties(this);
		cursorProperties.setTargetElementXpath(elementXpath);
		cursorProperties.setObjectActionType(EObjectActionType.MOVE);

		actionsQueue.add(cursorProperties);
		
		runAction();
	}

	public void event(MouseEvent event) {
		AnimationProperties cursorProperties = new AnimationProperties(this);
		cursorProperties.setEvent(event);
		actionsQueue.add(cursorProperties);
		runAction();
	}
	
	public void move(int x, int y) {
		AnimationProperties cursorProperties = new AnimationProperties(this);
		cursorProperties.setTargetPositionX(x);
		cursorProperties.setTargetPositionY(y);
		cursorProperties.setObjectActionType(EObjectActionType.MOVE);

		actionsQueue.add(cursorProperties);
		runAction();
	}

	private void waitUntilClickFinished(Element element, final ObjectWrapper<Boolean> finishIndicator) {
		EventListener eventListener = DOM.getEventListener(element);

		if (eventListener instanceof Widget) {
			HandlerManager handlerManager = WidgetHandlerProvider.getHandlerManager((Widget) eventListener);
			int count = handlerManager.getHandlerCount(ClickEvent.getType());

			final ObjectWrapper<Integer> listenersCount = new ObjectWrapper<Integer>();
			listenersCount.value = 0;
			
			for (int i = 0; i < count; i++) {
				ClickHandler clickHandler = handlerManager.getHandler(ClickEvent.getType(), i);
				
				if (clickHandler instanceof StateClickHandler) {
					listenersCount.value++;
				}
			}
			
			for (int i = 0; i < count; i++) {
				ClickHandler clickHandler = handlerManager.getHandler(ClickEvent.getType(), i);
				
				if (clickHandler instanceof StateClickHandler) {
					((StateClickHandler)clickHandler).addEventListener(new sk.seges.acris.player.client.listener.EventListener() {
						
						public void onSuccess() {
							listenersCount.value--;
							
							if (listenersCount.value == 0) {
								if (!finishIndicator.value) {
									finishIndicator.value = true;
									running.value = false;
									runAction();
								}
							}
						}
						
						public void onFailure() {
							GWT.log("Unable to continue with next action due to error on the page", null);
						}
					});
				}
			}
		} else {
			//not supported widget, execute next Action
			finishIndicator.value = true;
			running.value = false;
			runAction();
		}
	}

	private Element getElement(String xpath) {
		return cacheMap.resolveElement(xpath);
	}
	
	private Position getElementAbsolutePosition(Element element, boolean offset) {
		int x = DOM.getAbsoluteLeft(element);
		int y = DOM.getAbsoluteTop(element);
		
		if (offset) {
			x += element.getClientWidth() / 2;
			y += element.getClientHeight() / 2;
		}
		return new Position(x, y);
	}

	private static int counter = 0;
	
	void runAction(AnimationProperties cursorProperties) {
		if (cursorProperties.getObjectActionType() == null && cursorProperties.getEvent() != null ) {
		 cursorProperties.getEvent().fireEvent();
		 running.value = false;
			runAction();
//			timer = new Timer() {
//
//				public void run() {
//					running.value = false;
//					runAction();
//				}
//			};
//			timer.schedule(1000);
			
		} else if (cursorProperties.getObjectActionType().equals(EObjectActionType.MOVE)) {
			cursorProperties.setObjectPositionX(getPositionX());
			cursorProperties.setObjectPositionY(getPositionY());

			if (cursorProperties.getTargetElementXpath() != null) {
				Position position = getElementAbsolutePosition(getElement(cursorProperties.getTargetElementXpath()), true);
				cursorProperties.setTargetPositionX(position.left);
				cursorProperties.setTargetPositionY(position.top);
			}
//			PlayerModule.moveLog.setText(counter++ + ": move on position (" + cursorProperties.getTargetPositionX() + ", " + cursorProperties.getTargetPositionY() + ")");
			objectAnimation = new ObjectAnimation(this,
					cursorProperties);
			int duration = calculateDuration(cursorProperties);
			objectAnimation.run(duration);
			if (duration > 0 ) {
				objectAnimation.addAnimationEvent(new AnimationEvent() {
	
					public void onComplete() {
						running.value = false;
						runAction();
					}
				});
			} else {
				running.value = false;
				runAction();
			}
		} else if (cursorProperties.getObjectActionType().equals(EObjectActionType.CLICK)) {
//			PlayerModule.clickLog.setText(counter++ + ": Click on " + cursorProperties.getTargetElementId());

			Element element = getElement(cursorProperties.getTargetElementXpath());

			if (element != null) {
				clickElement(element);
			} else {
				throw new RuntimeException("Unknown element: " + cursorProperties.getTargetElementXpath());
			}

			if (cursorProperties.getWaitTime() > 0) {
				final ObjectWrapper<Boolean> finishedIndicator = new ObjectWrapper<Boolean>();
				finishedIndicator.value = false;
				waitUntilClickFinished(element, finishedIndicator);
				timer = new Timer() {

					public void run() {
						if (!finishedIndicator.value) {
							finishedIndicator.value = true;
							running.value = false;
							runAction();
						}
					}
				};
				timer.schedule(cursorProperties.getWaitTime());
			} else {
				running.value = false;
				runAction();
			}
		} else if (cursorProperties.getObjectActionType().equals(EObjectActionType.WAIT)) {
			timer = new Timer() {

				public void run() {
					running.value = false;
					runAction();
				}
			};
			timer.schedule(cursorProperties.getWaitTime());
		}
	}

	public static native void clickElement(Element elem) /*-{
        elem.click();
    }-*/;

	private synchronized void runAction() {
		if (running.value) {
			return;
		}
		if (actionsQueue.size() == 0) {
			return;
		}
		running.value = true;
		objectAnimation = null;
		timer = null;
		AnimationProperties cursorProperties = actionsQueue.get(0);
		actionsQueue.remove(0);
		cursorProperties.getAnimationObject().runAction(cursorProperties);
	}

	public void click(String elementXpath) {
		AnimationProperties cursorProperties = new AnimationProperties(this);
		cursorProperties.setTargetElementXpath(elementXpath);
		cursorProperties.setObjectActionType(EObjectActionType.CLICK);

		actionsQueue.add(cursorProperties);
		runAction();
	}

	public void clickAndWait(String elementXpath) {
		AnimationProperties cursorProperties = new AnimationProperties(this);
		cursorProperties.setTargetElementXpath(elementXpath);
		cursorProperties.setObjectActionType(EObjectActionType.CLICK);
		cursorProperties.setWaitTime(10000);
		actionsQueue.add(cursorProperties);
		runAction();
	}
	
	public void wait(int time) {
		AnimationProperties cursorProperties = new AnimationProperties(this);
		cursorProperties.setWaitTime(time);
		actionsQueue.add(cursorProperties);
		runAction();
	}

}
