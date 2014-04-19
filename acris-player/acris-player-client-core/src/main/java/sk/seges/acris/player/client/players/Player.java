package sk.seges.acris.player.client.players;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import sk.seges.acris.player.client.listener.CompleteHandler;
import sk.seges.acris.player.client.objects.*;
import sk.seges.acris.player.client.objects.common.EventMirror;
import sk.seges.acris.player.client.objects.common.EventProperties;
import sk.seges.acris.player.client.objects.common.HasPosition;
import sk.seges.acris.player.client.objects.utils.InputHolder;
import sk.seges.acris.player.client.players.event.ControlEvent;
import sk.seges.acris.player.client.players.view.ControlPanel;
import sk.seges.acris.player.client.players.view.ResourceBundle;
import sk.seges.acris.player.client.playlist.Playlist;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;
import sk.seges.acris.recorder.client.tools.CacheMap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Player {

	private List<EventMirror> eventMirrors = new ArrayList<EventMirror>();

	private Playlist playlist;
    private ControlPanel controlPanel = new ControlPanel();

	private List<EventProperties> actionsQueue = new LinkedList<EventProperties>();

	private boolean running = false;
    private int eventsToPlay = -1;

	public Player(boolean viewMode, int duration, CacheMap cacheMap) {

        InputHolder inputHolder = new InputHolder();

		eventMirrors.add(new MouseEventExecutor(viewMode, duration, cacheMap, inputHolder));
		eventMirrors.add(new HTMLEventExecutor());
		eventMirrors.add(new KeyboardEventExecutor());
//		eventMirrors.add(new WaitExecutor());
        eventMirrors.add(new ClipboardExecutor());
	}

	public boolean isPlaying() {
		return (playlist != null && this.playlist.getEventsCount() > 0);
	}

    public void showControlPanel(Panel panel) {
        controlPanel.addStyleName(ResourceBundle.INSTANCE.css().controlPanelTop());
        controlPanel.addControlEventHandler(new ControlEvent.ControlEventHandler() {
            @Override
            public void onControl(ControlEvent event) {
                switch (event.getControlType()) {
                    case PLAY:
                        play();
                        break;
                    case PAUSE:
                        pause();
                        break;
                    case NEXT:
                        eventsToPlay = eventMirrors.size();
                        runAction();
                        break;
                    default:
                        break;
                }
            }
        });
        panel.add(controlPanel);
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;

        if (playlist == null) {
            return;
        }

        for (EventMirror eventMirror: eventMirrors) {
            if (eventMirror.getWidget() != null) {
                RootPanel.get().add(eventMirror.getWidget());
            }
        }

        int count = playlist.getEventsCount();

        for (int i = 0; i < count; i++) {
            AbstractGenericEvent event = playlist.getEvent(i);
            for (EventMirror eventMirror: eventMirrors) {
                addEvent(event, eventMirror);
            }
        }

        controlPanel.showPlaylist(playlist);
    }

	public void play() {
        eventsToPlay = -1;
		runAction();
	}

	private void addEvent(AbstractGenericEvent event, EventMirror eventMirror) {
		EventProperties cursorProperties = new EventProperties(eventMirror);
		cursorProperties.setEvent(event);
		actionsQueue.add(cursorProperties);
	}

	public void pause() {
        if (running) {
            for (EventMirror eventMirror: eventMirrors) {
                eventMirror.destroy();
            }
        }
		running = false;
        eventsToPlay = 0;
	}

	private void runAction() {
		if (actionsQueue.size() == 0) {
			return;
		}

        if (eventsToPlay == 0) {
            return;
        }

        if (eventsToPlay > 0) {
            eventsToPlay--;
        }

        if (!running) {
            for (EventMirror eventMirror: eventMirrors) {
                eventMirror.initialize();
            }
        }

        running = true;

        if (actionsQueue.size() % eventMirrors.size() == 0) {
            playlist.selectNextEvent();
        }

		EventProperties cursorProperties = actionsQueue.get(0);
		actionsQueue.remove(0);
		EventMirror animationObject = cursorProperties.getEventMirror();

        if (animationObject instanceof HasPosition) {
            int y = ((HasPosition)animationObject).getObjectPositionY();

            if (y < (Window.getClientHeight() / 2)) {
                controlPanel.removeStyleName(ResourceBundle.INSTANCE.css().controlPanelTop());
                controlPanel.addStyleName(ResourceBundle.INSTANCE.css().controlPanelBottom());
            } else {
                controlPanel.removeStyleName(ResourceBundle.INSTANCE.css().controlPanelBottom());
                controlPanel.addStyleName(ResourceBundle.INSTANCE.css().controlPanelTop());
            }
        }

		animationObject.runAction(cursorProperties, new CompleteHandler() {
			@Override
			public void onComplete() {
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        runAction();
                    }
                });
			}
		});
	}
}
