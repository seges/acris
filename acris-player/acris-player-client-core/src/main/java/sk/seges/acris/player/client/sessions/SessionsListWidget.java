package sk.seges.acris.player.client.sessions;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;
import sk.seges.acris.recorder.client.session.RecordingSessionDetailParamsJSO;
import sk.seges.acris.recorder.shared.model.dto.RecordingSessionDTO;
import sk.seges.acris.widget.client.celltable.AbstractFilterableTable;

import java.util.Date;

public class SessionsListWidget extends AbstractFilterableTable<RecordingSessionDTO> {

	static class SessionsListKeyProvider implements ProvidesIdentifier<RecordingSessionDTO> {

		@Override
		public Object getKey(RecordingSessionDTO sessions) {
			return sessions.getId();
		}

		@Override
		public String getKeyColumnName() {
			return ID;
		}
	}

	public SessionsListWidget() {
		super(new SessionsListKeyProvider(), RecordingSessionDTO.class, false);

		setPageSize(10);

		initialize();
		initializeColumns();
	}

	private void initializeColumns() {

		Column<RecordingSessionDTO, String> initialUrlColumn = new Column<RecordingSessionDTO, String>(new TextCell()) {
			@Override
			public String getValue(RecordingSessionDTO session) {
				RecordingSessionDetailParamsJSO recordingSessionDetailParams = new RecordingSessionDetailParamsJSO(session.getSessionInfo());
				return recordingSessionDetailParams.getInitialUrl();
			};
		};
		addTextColumn(initialUrlColumn, 300, "Initial url", null, new StringValidator(), null);

		Column<RecordingSessionDTO, String> userAgentColumn = new Column<RecordingSessionDTO, String>(new TextCell()) {
			@Override
			public String getValue(RecordingSessionDTO session) {
				RecordingSessionDetailParamsJSO recordingSessionDetailParams = new RecordingSessionDetailParamsJSO(session.getSessionInfo());
				return recordingSessionDetailParams.getUserAgent();
			};
		};
		addTextColumn(userAgentColumn, 300, "User agent", null, new StringValidator(), null);

		Column<RecordingSessionDTO, String> ipAddressColumn = new Column<RecordingSessionDTO, String>(new TextCell()) {
			@Override
			public String getValue(RecordingSessionDTO session) {
				RecordingSessionDetailParamsJSO recordingSessionDetailParams = new RecordingSessionDetailParamsJSO(session.getSessionInfo());
				return recordingSessionDetailParams.getIpAddress();
			};
		};
		addTextColumn(ipAddressColumn, 100, "IP Address", null, new StringValidator(), null);

		Column<RecordingSessionDTO, Date> webIdColumn = new Column<RecordingSessionDTO, Date>(new DateCell()) {
			@Override
			public Date getValue(RecordingSessionDTO session) {
				return session.getSessionTime();
			};
		};
		addDateColumn(webIdColumn, 100, "Date", "sessionTime", new DateValidator(), null);

		addCheckboxColumn(50, null);
	}
}