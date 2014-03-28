package sk.seges.acris.player.server.service;

import sk.seges.acris.recorder.server.dao.api.RecordingLogDaoBase;
import sk.seges.acris.recorder.server.dao.api.RecordingSessionDaoBase;
import sk.seges.acris.recorder.server.domain.jpa.JpaRecordingSession;
import sk.seges.acris.recorder.server.model.data.RecordingLogData;
import sk.seges.acris.recorder.server.model.data.RecordingSessionData;
import sk.seges.sesam.dao.*;
import sk.seges.sesam.pap.service.annotation.LocalService;

import java.util.List;

@LocalService
public class PlayerService implements IPlayerRemoteServiceLocal {

	private final RecordingSessionDaoBase<RecordingSessionData> recordingSessionDao;
	private final RecordingLogDaoBase<RecordingLogData> recordingLogDao;

	public PlayerService(RecordingSessionDaoBase<RecordingSessionData> recordingSessionDao, RecordingLogDaoBase<RecordingLogData> recordingLogDao) {
		this.recordingSessionDao = recordingSessionDao;
		this.recordingLogDao = recordingLogDao;
	}

	@Override
	public PagedResult<List<RecordingLogData>> getLogs(Page page, RecordingSessionData session) {
		Criterion criterion = Filter.eq(RecordingLogData.SESSION + "." + RecordingSessionData.ID, session.getId());
		page.setFilterable(criterion);
		PagedResult<List<RecordingLogData>> result = recordingLogDao.findAll(page);

		for (RecordingLogData log: result.getResult()) {
			log.setEvent(log.getEvent().replace(' ', '\0'));
		}

		return result;
	}

	@Override
	public RecordingSessionData getSession(long sessionId) {
		JpaRecordingSession session = new JpaRecordingSession();
		session.setId(sessionId);
		return recordingSessionDao.findEntity(session);
	}

	@Override
	public PagedResult<List<RecordingSessionData>> getSessions(Page page, String webId, String language) {

		Conjunction conjunction = Filter.conjunction();

		if (webId != null) {
			conjunction.and(Filter.eq(RecordingSessionData.WEB_ID, webId));
		}

		if (language != null) {
			conjunction.and(Filter.eq(RecordingSessionData.LANGUAGE, language));
		}

		if (conjunction.getJunctions().size() > 0) {
			page.setFilterable(conjunction);
		}

		return recordingSessionDao.findAll(page);
	}
}