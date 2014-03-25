package sk.seges.acris.player.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import sk.seges.acris.player.client.players.TutorialPlayer;
import sk.seges.acris.player.client.view.SessionsPresenter;
import sk.seges.acris.player.client.view.SessionsView;
import sk.seges.acris.player.shared.service.IPlayerRemoteService;
import sk.seges.acris.player.shared.service.IPlayerRemoteServiceAsync;
import sk.seges.acris.player.shared.service.ServicesDefinition;
import sk.seges.acris.recorder.client.tools.CacheMap;

public class PlayerAction implements EntryPoint {

	private IPlayerRemoteServiceAsync initializeService() {
		IPlayerRemoteServiceAsync playerService = GWT.create(IPlayerRemoteService.class);
		ServiceDefTarget playerServiceEndPoint = (ServiceDefTarget) playerService;
		playerServiceEndPoint.setServiceEntryPoint(ServicesDefinition.PLAYER_SERVICE);
		return playerService;
	}

	public void onModuleLoad() {

		CacheMap cacheMap = new CacheMap(50);

		SessionsPresenter sessionsPresenter = new SessionsPresenter(new TutorialPlayer(cacheMap), new SessionsView(), initializeService());

		SimplePanel panel = new SimplePanel();
		sessionsPresenter.start(panel, new SimpleEventBus());

		RootPanel.get().add(panel);
	}
}
