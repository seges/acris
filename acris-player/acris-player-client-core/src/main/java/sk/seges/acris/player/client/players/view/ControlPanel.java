package sk.seges.acris.player.client.players.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import sk.seges.acris.player.client.players.event.ControlEvent;
import sk.seges.acris.player.client.playlist.Playlist;
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

    @UiField ListBox playListBox;
    @UiField Label timerLabel;

    private Playlist playlist;

    public ControlPanel() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    private void showInfo() {
        int selected = this.playListBox.getSelectedIndex();

        if (this.playlist.getEventsCount() > selected + 1) {
            //has more items
            AbstractGenericEvent event = this.playlist.getEvent(selected + 1);
            showTimer(event.getDeltaTime());
        }
    }

    private static final int PERIOD = 100;
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
                    cancel();
                    timer = null;
                }
                timerLabel.setText(current + " ms left");
                current -= PERIOD;
            }
        };
        timer.scheduleRepeating(PERIOD);

        timerLabel.setText(ms + " ms left");
    }

    @UiHandler("playButton")
    public void onPlay(ClickEvent event) {
        fireEvent(new ControlEvent(ControlEvent.ControlType.PLAY));
        showInfo();
    }

    @UiHandler("pauseButton")
    public void onPause(ClickEvent event) {
        fireEvent(new ControlEvent(ControlEvent.ControlType.PAUSE));
    }

    @UiHandler("nextButton")
    public void onNext(ClickEvent event) {
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
		    this.playListBox.addItem(event.toString(true, false));
		}
		this.playListBox.setSelectedIndex(0);
   }

    @Override
    public HandlerRegistration addControlEventHandler(ControlEvent.ControlEventHandler handler) {
        return this.addHandler(handler, ControlEvent.getType());
    }
}