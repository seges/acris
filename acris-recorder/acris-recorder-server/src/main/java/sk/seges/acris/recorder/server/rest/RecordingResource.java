package sk.seges.acris.recorder.server.rest;

import sk.seges.acris.recorder.server.model.data.RecordingSessionData;
import sk.seges.acris.recorder.server.service.RecordingLocalService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("/record")
public class RecordingResource {

	public class RecordingSessionValue {

		private final RecordingSessionData sessionData;

		public RecordingSessionValue(RecordingSessionData sessionData) {
			this.sessionData = sessionData;
		}

		public String getLanguage() {
			return sessionData.getLanguage();
		}

		public String getSessionInfo() {
			return sessionData.getSessionInfo();
		}

		public Date getSessionTime() {
			return sessionData.getSessionTime();
		}
	}

	private final RecordingLocalService recordingLocalService;

	public RecordingResource(RecordingLocalService recordingLocalService) {
		this.recordingLocalService = recordingLocalService;
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/sessions")
	public List<RecordingSessionValue> getRecordingSessions() {
		List<RecordingSessionData> sessions = recordingLocalService.getRecordingSessions();

		List<RecordingSessionValue> result = new ArrayList<RecordingSessionValue>();

		for (RecordingSessionData session: sessions) {
			result.add(new RecordingSessionValue(session));
		}

		return result;
	}
}