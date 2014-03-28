package sk.seges.acris.player.client.players;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetHandlerProvider;
import sk.seges.acris.player.client.event.handler.StateClickHandler;
import sk.seges.acris.player.client.listener.CompleteHandler;
import sk.seges.acris.player.client.objects.HTMLEventExecutor;
import sk.seges.acris.player.client.objects.KeyboardEventExecutor;
import sk.seges.acris.player.client.objects.MouseEventExecutor;
import sk.seges.acris.player.client.objects.common.EventMirror;
import sk.seges.acris.player.client.objects.common.EventProperties;
import sk.seges.acris.player.client.playlist.Playlist;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;
import sk.seges.acris.recorder.client.tools.CacheMap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Player {

	private List<EventMirror> eventMirrors = new ArrayList<EventMirror>();

	private Playlist playlist;
	private boolean showPlaylist;

	private List<EventProperties> actionsQueue = new LinkedList<EventProperties>();

	private boolean running = false;

	public Player(int duration, CacheMap cacheMap) {

		eventMirrors.add(new MouseEventExecutor(duration, cacheMap));
		eventMirrors.add(new HTMLEventExecutor());
		eventMirrors.add(new KeyboardEventExecutor());

		running = false;
	}

	public boolean isPlaying() {
		return (playlist != null && this.playlist.getEventsCount() > 0);
	}

	public void showPlaylist() {
		if (isPlaying()) {
			playlist.show();
		} else {
			showPlaylist = true;
		}
	}
	
	public void play(Playlist playlist) {
		this.playlist = playlist;

		if (showPlaylist) {
			showPlaylist();
		}

		for (EventMirror eventMirror: eventMirrors) {
			if (eventMirror.getWidget() != null) {
				RootPanel.get().add(eventMirror.getWidget());
			}
			eventMirror.initialize();
		}

		int count = playlist.getEventsCount();

		for (int i = 0; i < count; i++) {
			AbstractGenericEvent event = playlist.getEvent(i);
			for (EventMirror eventMirror: eventMirrors) {
				addEvent(event, eventMirror);
			}
		}

		runAction();
	}

	private void addEvent(AbstractGenericEvent event, EventMirror eventMirror) {
		EventProperties cursorProperties = new EventProperties(eventMirror);
		cursorProperties.setEvent(event);
		actionsQueue.add(cursorProperties);
	}

	public void stop() {
		for (EventMirror eventMirror: eventMirrors) {
			eventMirror.destroy();
		}

		running = false;
	}

	class ObjectWrapper<T> {
		T value;
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
//									running.value = false;
//									runAction();
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
//			running = false;
//			runAction();
		}
	}

	private synchronized void runAction() {
		if (running) {
			return;
		}
		if (actionsQueue.size() == 0) {
			return;
		}
		running = true;
//		objectAnimation = null;
//		timer = null;
		EventProperties cursorProperties = actionsQueue.get(0);
		actionsQueue.remove(0);
		EventMirror animationObject = cursorProperties.getEventMirror();
		animationObject.runAction(cursorProperties, new CompleteHandler() {
			@Override
			public void onComplete() {
				running = false;
				playlist.selectNextEvent();
				runAction();
			}
		});
	}
}
