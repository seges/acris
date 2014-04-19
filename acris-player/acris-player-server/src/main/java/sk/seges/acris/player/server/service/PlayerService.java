package sk.seges.acris.player.server.service;

import org.springframework.transaction.annotation.Transactional;
import sk.seges.acris.recorder.server.dao.api.RecordingBlobDaoBase;
import sk.seges.acris.recorder.server.dao.api.RecordingLogDaoBase;
import sk.seges.acris.recorder.server.dao.api.RecordingSessionDaoBase;
import sk.seges.acris.recorder.server.domain.jpa.JpaRecordingBlobPk;
import sk.seges.acris.recorder.server.domain.jpa.JpaRecordingSession;
import sk.seges.acris.recorder.server.model.data.RecordingBlobData;
import sk.seges.acris.recorder.server.model.data.RecordingLogData;
import sk.seges.acris.recorder.server.model.data.RecordingSessionData;
import sk.seges.acris.server.domain.converter.BlobConverter;
import sk.seges.sesam.dao.Criterion;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.pap.service.annotation.LocalService;

import javax.persistence.EntityManager;
import java.util.List;

@LocalService
public class PlayerService implements IPlayerRemoteServiceLocal {

	private final RecordingSessionDaoBase<RecordingSessionData> recordingSessionDao;
	private final RecordingLogDaoBase<RecordingLogData> recordingLogDao;
    private final RecordingBlobDaoBase<RecordingBlobData> recordingBlobDao;
    private final EntityManager entityManager;

	public PlayerService(EntityManager entityManager, RecordingSessionDaoBase<RecordingSessionData> recordingSessionDao, RecordingLogDaoBase<RecordingLogData> recordingLogDao,
                         RecordingBlobDaoBase<RecordingBlobData> recordingBlobDao) {

        assert entityManager != null;

        this.entityManager = entityManager;
		this.recordingSessionDao = recordingSessionDao;
		this.recordingLogDao = recordingLogDao;
        this.recordingBlobDao = recordingBlobDao;
	}

	@Override
	public PagedResult<List<RecordingLogData>> getLogs(Page page, RecordingSessionData session) {
		Criterion criterion = Filter.eq(RecordingLogData.SESSION + "." + RecordingSessionData.ID, session.getId());
		page.setFilterable(criterion);
		return recordingLogDao.findAll(page);
	}

    @Override
    @Transactional
    public String getRecodedBlob(long sessionId, long blobId) {
        JpaRecordingBlobPk pk = new JpaRecordingBlobPk();
        pk.setBlobId(blobId);
        pk.setSessionId(sessionId);
        RecordingBlobData entityInstance = recordingBlobDao.getEntityInstance();
        entityInstance.setId(pk);
        RecordingBlobData result = recordingBlobDao.findEntity(entityInstance);
        if (result == null) {
            throw new RuntimeException("Unable to find blob with id " + blobId + " for session with id " + sessionId);
        }

        BlobConverter blobConverter = new BlobConverter(entityManager);
        return blobConverter.toDto(result.getContent());
    }

    @Override
	public RecordingSessionData getSession(long sessionId) {
		JpaRecordingSession session = new JpaRecordingSession();
		session.setId(sessionId);
		return recordingSessionDao.findEntity(session);
	}

	@Override
	public PagedResult<List<RecordingSessionData>> getSessions(Page page) {
		return recordingSessionDao.findAll(page);
	}
}