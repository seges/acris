package sk.seges.acris.player.client.session;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import sk.seges.acris.player.client.players.TutorialPlayer;
import sk.seges.acris.player.shared.service.IPlayerRemoteService;
import sk.seges.acris.player.shared.service.IPlayerRemoteServiceAsync;
import sk.seges.acris.player.shared.service.ServicesDefinition;
import sk.seges.acris.recorder.client.tools.CacheMap;
import sk.seges.acris.recorder.shared.model.dto.RecordingSessionDTO;

/**
 * Created by PeterSimun on 11.4.2014.
 */
public class SessionPlayer {

    private final CacheMap cacheMap = new CacheMap(50);
    private IPlayerRemoteServiceAsync playerService;

    private IPlayerRemoteServiceAsync ensureService() {
        if (playerService == null) {
            playerService = GWT.create(IPlayerRemoteService.class);
            ServiceDefTarget playerServiceEndPoint = (ServiceDefTarget) playerService;
            playerServiceEndPoint.setServiceEntryPoint(ServicesDefinition.PLAYER_SERVICE);
        }

        return playerService;
    }

    public void loadSession(long id) {

        ensureService().getSession(id, new AsyncCallback<RecordingSessionDTO>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new RuntimeException(caught);
            }

            @Override
            public void onSuccess(RecordingSessionDTO result) {
                replaySession(result);
            }
        });
    }

    public void replaySession(RecordingSessionDTO result) {
        SessionPresenter presenter = new SessionPresenter(new TutorialPlayer(true, cacheMap), ensureService(), cacheMap);
        SimplePanel panel = new SimplePanel();
        presenter.start(panel, new SimpleEventBus());
        presenter.initialize(result);

        RootPanel.get().add(panel);
    }
}