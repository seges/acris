package sk.seges.acris.player.client.players.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import sk.seges.acris.player.client.players.event.ControlEvent;
import sk.seges.acris.player.client.playlist.Playlist;
import sk.seges.acris.recorder.client.event.HasAbsolutePosition;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;
import sk.seges.sesam.handler.ValueChangeHandler;

/**
 * Created by PeterSimun on 11.4.2014.
 */
public class ControlPanel extends Composite implements ControlPanelDisplay {

    interface ControlPanelUiBinder extends UiBinder<HTMLPanel, ControlPanel> {}
    private final ControlPanelUiBinder uiBinder = GWT.create(ControlPanelUiBinder.class);

    @UiField Button playButton;
    @UiField Button pauseButton;
    @UiField Button nextButton;
    @UiField HTML statusLabel;
    @UiField HTML eventInfoLabel;

    @UiField ListBox playListBox;
    @UiField Label timerLabel;

    private Playlist playlist;
    private PanelPosition position = PanelPosition.TOP;

    public ControlPanel() {
        initWidget(uiBinder.createAndBindUi(this));
        playListBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                int selectedIndex = playListBox.getSelectedIndex();
                AbstractGenericEvent event = playlist.getEvent(selectedIndex);
                showStatus(ControlEvent.ControlType.SPECIFIC);
                if (timer != null) {
                    timer.cancel();
                }
                fireEvent(new ControlEvent(ControlEvent.ControlType.SPECIFIC, event, selectedIndex));
            }
        });
    }

    private void showInfo() {
        int selected = this.playListBox.getSelectedIndex();

        if (this.playlist.getEventsCount() > selected + 1) {
            //has more items
            AbstractGenericEvent event = this.playlist.getEvent(selected + 1);
            if (event instanceof HasAbsolutePosition) {
                HasAbsolutePosition hasPoistionEvent = (HasAbsolutePosition) event;
                eventInfoLabel.setHTML("X: " + hasPoistionEvent.getAbsoluteClientX() + "<br/>" +
                                       "Y:" + hasPoistionEvent.getAbsoluteClientY());
            } else {
                eventInfoLabel.setHTML("-");
            }
            showTimer(event.getDeltaTime());
        }
    }

    private static final int PERIOD = 10;
    private Timer timer;

    private void showTimer(final int ms) {

        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer() {
            int current = ms;

            @Override
            public void run() {
                if (current < 0) {
                    timerLabel.setText("0 ms left");
                    cancel();
                    timer = null;
                } else {
                    timerLabel.setText(current + " ms left");
                    current -= PERIOD;
                }
            }
        };
        timer.scheduleRepeating(PERIOD);

        timerLabel.setText(ms + " ms left");
    }

    @UiHandler("playButton")
    public void onPlay(ClickEvent event) {
        showStatus(ControlEvent.ControlType.PLAY);
        fireEvent(new ControlEvent(ControlEvent.ControlType.PLAY));
        showInfo();
    }

    @UiHandler("pauseButton")
    public void onPause(ClickEvent event) {
        showStatus(ControlEvent.ControlType.PAUSE);
        if (timer != null) {
            timer.cancel();
        }
        fireEvent(new ControlEvent(ControlEvent.ControlType.PAUSE));
    }

    @UiHandler("nextButton")
    public void onNext(ClickEvent event) {
        showStatus(ControlEvent.ControlType.NEXT);
        fireEvent(new ControlEvent(ControlEvent.ControlType.NEXT));
    }

    @Override
    public void showPlaylist(Playlist playlist) {

        this.playlist = playlist;

        playlist.addValueChangeHandler(new ValueChangeHandler<Integer>() {
            @Override
            public void onValueChanged(Integer oldValue, Integer newValue) {
                playListBox.setSelectedIndex(newValue);
                showInfo();
            }
        });

		int count = playlist.getEventsCount();

		for (int j = 0; j < count; j++) {
			AbstractGenericEvent event = playlist.getEvent(j);
		    this.playListBox.addItem(j + ":" + event.toString(true, false));
		}
		this.playListBox.setSelectedIndex(0);
   }

    @Override
    public void showStatus(ControlEvent.ControlType status) {
        switch (status) {
            case SPECIFIC:
                statusLabel.setHTML("Running events <br/>to the selected...");
                break;
            case PAUSE:
                statusLabel.setText("Paused");
                break;
            case PLAY:
                statusLabel.setText("Running");
                break;
            case NEXT:
                statusLabel.setText("Running 1 [next] event...");
                break;
            case PREVIOUS:
                statusLabel.setText("Running 1 [previous] event...");
                break;
            case STOP:
                statusLabel.setText("Finished");
                break;
        }
    }

    @Override
    public void setPosition(PanelPosition position) {
        if (position.equals(this.position)) {
            return;
        }
        this.position = position;

        switch (position) {
            case TOP:
                removeStyleName(ResourceBundle.INSTANCE.css().controlPanelBottom());
                addStyleName(ResourceBundle.INSTANCE.css().controlPanelTop());
                break;
            case BOTTOM:
                removeStyleName(ResourceBundle.INSTANCE.css().controlPanelTop());
                addStyleName(ResourceBundle.INSTANCE.css().controlPanelBottom());
                break;
            default:
                throw new RuntimeException("Unknown position type: " + position);
        }
    }

    @Override
    public HandlerRegistration addControlEventHandler(ControlEvent.ControlEventHandler handler) {
        return this.addHandler(handler, ControlEvent.getType());
    }
}