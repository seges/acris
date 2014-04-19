package sk.seges.acris.player.client.players.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Created by PeterSimun on 11.4.2014.
 */
public interface ResourceBundle extends ClientBundle {

    public static final ResourceBundle INSTANCE = GWT.create(ResourceBundle.class);

    @Source(ControlPanelStyle.DEFAULT_CSS)
    ControlPanelStyle css();

    @Source("media-play.png")
    ImageResource play();

    @Source("media-pause.png")
    ImageResource pause();

    @Source("media-forward.png")
    ImageResource next();
}