package sk.seges.acris.player.client.sessions;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.SingleSelectionModel;
import sk.seges.acris.player.client.players.event.PlayEvent;
import sk.seges.acris.recorder.shared.model.dto.RecordingSessionDTO;
import sk.seges.sesam.shared.model.dto.PagedResultDTO;

import java.util.List;

public class SessionsView extends Composite implements SessionsPresenter.SessionsDisplay {

	@UiField
	SessionsListWidget sessionsListWidget;

    @UiField
    SimplePager paginator;

    @UiField
	Button playButton;

	interface Binder extends UiBinder<HTMLPanel, SessionsView> {}
	private final Binder uiBinder = GWT.create(Binder.class);

	public SessionsView() {
		initWidget(uiBinder.createAndBindUi(this));
        paginator.setDisplay(sessionsListWidget);
	}

	public void setData(PagedResultDTO<List<RecordingSessionDTO>> sessions) {
		sessionsListWidget.setRowCount(sessions.getTotalResultCount());
		sessionsListWidget.setRowData(sessions.getResult());
        paginator.setPageSize(sessions.getPage().getPageSize());
        paginator.setPageStart(sessions.getPage().getStartIndex());
	}

	@UiHandler("playButton")
	public void playButtonHandler(ClickEvent event) {
		RecordingSessionDTO selectedSession = ((SingleSelectionModel<RecordingSessionDTO>)sessionsListWidget.getSelectionModel()).getSelectedObject();

		if (selectedSession == null) {
			Window.alert("Choose session from the list!");
			return;
		}

		fireEvent(new PlayEvent(selectedSession));
	}

	@Override
	public HandlerRegistration addPlayHandler(PlayEvent.PlayHandler handler) {
		return addHandler(handler, PlayEvent.getType());
	}
}