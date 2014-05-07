package sk.seges.acris.player.client.players;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;
import sk.seges.acris.player.client.listener.CompleteHandler;
import sk.seges.acris.player.client.objects.ClipboardExecutor;
import sk.seges.acris.player.client.objects.HTMLEventExecutor;
import sk.seges.acris.player.client.objects.KeyboardEventExecutor;
import sk.seges.acris.player.client.objects.MouseEventExecutor;
import sk.seges.acris.player.client.objects.common.AnimationObject;
import sk.seges.acris.player.client.objects.common.EventMirror;
import sk.seges.acris.player.client.objects.common.EventProperties;
import sk.seges.acris.player.client.objects.common.HasPosition;
import sk.seges.acris.player.client.objects.utils.InputHolder;
import sk.seges.acris.player.client.players.event.ControlEvent;
import sk.seges.acris.player.client.players.view.ControlPanel;
import sk.seges.acris.player.client.players.view.ControlPanelDisplay;
import sk.seges.acris.player.client.players.view.ResourceBundle;
import sk.seges.acris.player.client.playlist.Playlist;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;

import java.util.*;

public class Player {

	private List<EventMirror> eventMirrors = new ArrayList<EventMirror>();

	private Playlist playlist;
    private ControlPanel controlPanel = new ControlPanel();
    private SimpleEventBus eventBus = new SimpleEventBus();

	private List<AbstractGenericEvent> actionsQueue = new LinkedList<AbstractGenericEvent>();
    private List<AbstractGenericEvent> executedEvents = new LinkedList<AbstractGenericEvent>();

	private boolean running = false;
    private int eventsToPlay = -1;

	public Player(boolean viewMode, int speed) {

        InputHolder inputHolder = new InputHolder();

		eventMirrors.add(new MouseEventExecutor(viewMode, speed, inputHolder));
		eventMirrors.add(new HTMLEventExecutor());
		eventMirrors.add(new KeyboardEventExecutor());
//		eventMirrors.add(new WaitExecutor());
        eventMirrors.add(new ClipboardExecutor());

        changeSpeed(speed);
	}

	public boolean isPlaying() {
		return (playlist != null && this.playlist.getEventsCount() > 0);
	}

    private HandlerRegistration handlerRegistration;

    public void showControlPanel(Panel panel) {
        eventBus.addHandler(ControlEvent.getType(), new ControlEvent.ControlEventHandler() {
            @Override
            public void onControl(ControlEvent event) {
                controlPanel.showStatus(event.getControlType());
            }
        });

        controlPanel.addStyleName(ResourceBundle.INSTANCE.css().controlPanelTop());
        controlPanel.addControlEventHandler(new ControlEvent.ControlEventHandler() {
            @Override
            public void onControl(ControlEvent event) {
                switch (event.getControlType()) {
                    case PLAY:
                        play();
                        break;
                    case SPECIFIC:
                        int index = event.getEventIndex() - executedEvents.size();

                        if (index < 0) {
                            //unable to play past event
                            return;
                        }

                        eventsToPlay = index;

                        final int previousSpeed = changeSpeed(AnimationObject.NO_SPEED);
                        handlerRegistration = eventBus.addHandler(ControlEvent.getType(), new ControlEvent.ControlEventHandler() {
                            @Override
                            public void onControl(ControlEvent event) {
                                changeSpeed(previousSpeed);
                                eventsToPlay = -1;
                                handlerRegistration.removeHandler();
                                if (event.getControlType().equals(ControlEvent.ControlType.PAUSE)) {
                                    //runAction();
                                }
                            }
                        });

                        runAction();
                        break;
                    case PAUSE:
                        pause();
                        break;
                    case NEXT:
                        eventsToPlay = 1;
                        runAction();
                        break;
                    default:
                        break;
                }
            }
        });
        panel.add(controlPanel);
    }

    protected int changeSpeed(int speed) {
        int result = -1;

        for (EventMirror eventMirror: eventMirrors) {
            result = eventMirror.getSpeed();
            eventMirror.setSpeed(speed);
        }

        return result;
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
            actionsQueue.add(playlist.getEvent(i));
        }

        controlPanel.showPlaylist(playlist);
    }

	public void play() {
        eventsToPlay = -1;
		runAction();
	}

	public void pause() {
        if (running) {
//            for (EventMirror eventMirror: eventMirrors) {
//                eventMirror.destroy();
//            }
        }
		running = false;
        eventsToPlay = 0;
	}

    long lastTime = 0;

    private void runAction() {

        if (actionsQueue.size() == 0) {
            eventBus.fireEvent(new ControlEvent(ControlEvent.ControlType.STOP));
			return;
		}

        if (eventsToPlay == 0) {
            eventBus.fireEvent(new ControlEvent(ControlEvent.ControlType.PAUSE));
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

        long currentTime = new Date().getTime();

        if (lastTime != 0) {
            GWT.log("Last event " + executedEvents.get(executedEvents.size() - 1).toString(true, false) + " took " + (currentTime - lastTime) + " ms");
        }
        lastTime = currentTime;

        playlist.selectNextEvent();

        AbstractGenericEvent event = actionsQueue.get(0);
        actionsQueue.remove(0);
        executedEvents.add(event);

        runAction(event, eventMirrors.iterator());
    };

    private int i = 0;

    private void runAction(final AbstractGenericEvent event, final Iterator<EventMirror> eventMirrorIterator) {

        if (eventMirrorIterator.hasNext()) {
            EventMirror eventMirror = eventMirrorIterator.next();

            final EventProperties eventProperties = new EventProperties(eventMirror);
            eventProperties.setEvent(event);

            final EventMirror animationObject = eventProperties.getEventMirror();

            if (animationObject instanceof HasPosition) {
                int y = ((HasPosition)animationObject).getObjectPositionY();

                if (y < (Window.getClientHeight() / 2)) {
                    controlPanel.setPosition(ControlPanelDisplay.PanelPosition.BOTTOM);
                } else {
                    controlPanel.setPosition(ControlPanelDisplay.PanelPosition.TOP);
                }
            }

            runAction(event, eventMirrorIterator);

            animationObject.runAction(eventProperties, new CompleteHandler() {
                @Override
                public void onComplete() {
                    if (++i == eventMirrors.size()) {
                        i = 0;
                        Scheduler.get().scheduleDeferred(new Command() {
                            @Override
                            public void execute() {
                                runAction();
                            }
                        });
                    }
                }
            });
        }
	}
}