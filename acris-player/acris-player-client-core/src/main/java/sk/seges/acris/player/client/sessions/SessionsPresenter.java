package sk.seges.acris.player.client.sessions;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import sk.seges.acris.player.client.configuration.PresenterProvider;
import sk.seges.acris.player.client.players.event.PlayEvent;
import sk.seges.acris.player.client.session.SessionPlayer;
import sk.seges.acris.player.client.session.SessionPresenter;
import sk.seges.acris.player.shared.service.IPlayerRemoteServiceAsync;
import sk.seges.acris.recorder.server.model.data.RecordingSessionData;
import sk.seges.acris.recorder.shared.model.dto.RecordingSessionDTO;
import sk.seges.sesam.dao.Conjunction;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.shared.model.dao.SortInfo;
import sk.seges.sesam.shared.model.dto.ConjunctionDTO;
import sk.seges.sesam.shared.model.dto.FilterDTO;
import sk.seges.sesam.shared.model.dto.PageDTO;
import sk.seges.sesam.shared.model.dto.PagedResultDTO;

import java.util.ArrayList;
import java.util.List;

public class SessionsPresenter extends AbstractActivity implements PlayEvent.PlayHandler {

	public interface SessionsDisplay extends IsWidget, PlayEvent.HasPlayHandlers {

		void setData(PagedResultDTO<List<RecordingSessionDTO>> sessions);
	}

	private final SessionsDisplay display;
	private final IPlayerRemoteServiceAsync playerService;
	private final PresenterProvider<SessionPresenter> sessionPresenterProvider;

	private AcceptsOneWidget parentPanel;

	public SessionsPresenter(PresenterProvider<SessionPresenter> sessionPresenterProvider, SessionsDisplay display, IPlayerRemoteServiceAsync playerService) {
		this.display = display;
		this.sessionPresenterProvider = sessionPresenterProvider;
		this.playerService = playerService;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {

		this.parentPanel = panel;

		panel.setWidget(display);

		PageDTO page = new PageDTO(0, 10);
        List<SortInfo> sortables = new ArrayList<SortInfo>();
        sortables.add(new SortInfo(false, RecordingSessionDTO.SESSION_TIME));
        page.setProjectableResult("sk.seges.acris.recorder.shared.model.dto.RecordingSessionDTO");
        page.setSortables(sortables);

        ConjunctionDTO conjunction = FilterDTO.conjunction();

        String webId = null;

        if (webId != null) {
            conjunction.and(FilterDTO.eq(RecordingSessionDTO.WEB_ID, webId));
        }

        String language = null;

        if (language != null) {
            conjunction.and(FilterDTO.eq(RecordingSessionDTO.LANGUAGE, language));
        }

        if (conjunction.getJunctions().size() > 0) {
            page.setFilterable(conjunction);
        }

        playerService.getSessions(page, new AsyncCallback<PagedResultDTO<List<RecordingSessionDTO>>>() {

			@Override
			public void onFailure(Throwable caught) {
				throw new RuntimeException(caught);
			}

			@Override
			public void onSuccess(PagedResultDTO<List<RecordingSessionDTO>> result) {
				display.setData(result);
			}
		});

		//TODO deregister somewhere
		display.addPlayHandler(this);
	}

	@Override
	public void onPlay(final PlayEvent event) {
        ((Widget)parentPanel).getElement().removeFromParent();
        new SessionPlayer().replaySession(event.getRecordingSession());
	}
}