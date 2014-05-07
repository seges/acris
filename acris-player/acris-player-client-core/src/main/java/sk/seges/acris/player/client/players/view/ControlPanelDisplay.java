package sk.seges.acris.player.client.players.view;

import sk.seges.acris.player.client.players.event.ControlEvent;
import sk.seges.acris.player.client.playlist.Playlist;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;

/**
 * Created by PeterSimun on 11.4.2014.
 */
public interface ControlPanelDisplay extends ControlEvent.HasControlEventHandlers {

    public enum PanelPosition {
        TOP, BOTTOM;
    };

    void showPlaylist(Playlist playlist);

    void showStatus(ControlEvent.ControlType status);

    void setPosition(PanelPosition position);
}
