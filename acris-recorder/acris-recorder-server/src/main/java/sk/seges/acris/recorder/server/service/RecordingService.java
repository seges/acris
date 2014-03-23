package sk.seges.acris.recorder.server.service;

import org.springframework.transaction.annotation.Transactional;
import sk.seges.acris.recorder.server.dao.api.RecordingLogDaoBase;
import sk.seges.acris.recorder.server.dao.api.RecordingSessionDaoBase;
import sk.seges.acris.recorder.server.model.data.RecordingLogData;
import sk.seges.acris.recorder.server.model.data.RecordingSessionData;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.pap.service.annotation.LocalService;

import java.util.List;

@LocalService
public class RecordingService implements RecordingLocalService {

	private final RecordingSessionDaoBase<RecordingSessionData> recordingSessionDao;
	private final RecordingLogDaoBase<RecordingLogData> recordingLogDao;

	public RecordingService(RecordingSessionDaoBase<RecordingSessionData> recordingSessionDao, RecordingLogDaoBase<RecordingLogData> recordingLogDao) {
		this.recordingSessionDao = recordingSessionDao;
		this.recordingLogDao = recordingLogDao;
	}

	@Override
	@Transactional
	public void recordLog(RecordingLogData recordingLog) {
		recordingLog.setEvent(recordingLog.getEvent().replace('\0', ' '));
		recordingLogDao.persist(recordingLog);
	}

	@Override
	@Transactional
	public RecordingSessionData startSession(RecordingSessionData session) {
		return recordingSessionDao.persist(session);
	}

	@Override
	public List<RecordingSessionData> getRecordingSessions() {
		return recordingSessionDao.findAll(Page.ALL_RESULTS_PAGE).getResult();
	}
}