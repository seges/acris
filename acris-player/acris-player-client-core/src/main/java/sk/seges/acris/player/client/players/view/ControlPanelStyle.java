package sk.seges.acris.player.client.players.view;

import com.google.gwt.resources.client.CssResource;

/**
 * Created by PeterSimun on 11.4.2014.
 */
public interface ControlPanelStyle extends CssResource {

    String DEFAULT_CSS = "sk/seges/acris/player/client/players/view/ControlPanel.css";

    String controlPanel();
    String controlPanelTop();
    String controlPanelBottom();

    String sessionHolder();

    String playButton();
    String pauseButton();
    String nextButton();

    String disabled();

    String playlist();
    String timer();
}
