package sk.seges.acris.recorder.server.rest;

import sk.seges.acris.player.server.service.IPlayerRemoteServiceLocal;
import sk.seges.acris.recorder.server.domain.jpa.JpaRecordingSession;
import sk.seges.acris.recorder.server.model.data.RecordingLogData;
import sk.seges.acris.recorder.server.model.data.RecordingSessionData;
import sk.seges.sesam.dao.Page;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("/player")
public class PlayerResource {

	public class RecordingSessionValue {

		private final RecordingSessionData sessionData;

		public RecordingSessionValue(RecordingSessionData sessionData) {
			this.sessionData = sessionData;
		}

        public Long getId() {
            return sessionData.getId();
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

	private final IPlayerRemoteServiceLocal playerLocalService;

	public PlayerResource(IPlayerRemoteServiceLocal playerLocalService) {
		this.playerLocalService = playerLocalService;
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/sessions")
	public List<RecordingSessionValue> getRecordingSessions() {
        return getRecordingSessions(null, null);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/sessions/page-index/{page-index}/page-size/{page-size}")
    public List<RecordingSessionValue> getRecordingSessions(@PathParam("page-index") Integer pageIndex, @PathParam("page-size") Integer pageSize) {

        if (pageIndex == null) {
            pageIndex = 0;
        }
        if (pageSize == null) {
            pageSize = Page.ALL_RESULTS;
        }
		List<RecordingSessionData> sessions = playerLocalService.getSessions(new Page(pageIndex, pageSize)).getResult();

		List<RecordingSessionValue> result = new ArrayList<RecordingSessionValue>();

		for (RecordingSessionData session: sessions) {
			result.add(new RecordingSessionValue(session));
		}

		return result;
	}

    public class RecordingSessionLogsValue {

        private final RecordingLogData sessionLogData;

        public RecordingSessionLogsValue(RecordingLogData sessionLogData) {
            this.sessionLogData = sessionLogData;
        }

        public String getEvent() {
            return sessionLogData.getEvent();
        }

        public Date getEventTime() {
            return sessionLogData.getEventTime();
        }
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/session/{session-id}")
    public List<RecordingSessionLogsValue> getRecordingLogs(@PathParam("session-id") Long sessionId) {
        return getRecordingLogs(sessionId, null, null);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/session/{session-id}/page-index/{page-index}/page-size/{page-size}")
    public List<RecordingSessionLogsValue> getRecordingLogs(@PathParam("session-id") Long sessionId, @PathParam("page-index") Integer pageIndex,
                                                            @PathParam("page-size") Integer pageSize) {
        if (sessionId == null) {
            throw new RuntimeException("Missing sessionId parameter");
        }

        if (pageIndex == null) {
            pageIndex = 0;
        }
        if (pageSize == null) {
            pageSize = Page.ALL_RESULTS;
        }

        JpaRecordingSession session = new JpaRecordingSession();
        session.setId(sessionId);
        List<RecordingLogData> logs = playerLocalService.getLogs(new Page(pageIndex, pageSize), session).getResult();

        List<RecordingSessionLogsValue> result = new ArrayList<RecordingSessionLogsValue>();

        for (RecordingLogData log: logs) {
            result.add(new RecordingSessionLogsValue(log));
        }

        return result;
    }
}