package sk.seges.acris.recorder.server.service;

import org.springframework.transaction.annotation.Transactional;
import sk.seges.acris.recorder.server.dao.api.RecordingBlobDaoBase;
import sk.seges.acris.recorder.server.dao.api.RecordingLogDaoBase;
import sk.seges.acris.recorder.server.dao.api.RecordingSessionDaoBase;
import sk.seges.acris.recorder.server.domain.jpa.JpaRecordingBlobPk;
import sk.seges.acris.recorder.server.model.data.RecordingBlobData;
import sk.seges.acris.recorder.server.model.data.RecordingLogData;
import sk.seges.acris.recorder.server.model.data.RecordingSessionData;
import sk.seges.acris.recorder.server.util.BlobHelper;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.pap.service.annotation.LocalService;

import java.nio.charset.Charset;
import java.util.List;

@LocalService
public class RecordingService implements IRecordingRemoteServiceLocal {

    private final RecordingSessionDaoBase<RecordingSessionData> recordingSessionDao;
    private final RecordingLogDaoBase<RecordingLogData> recordingLogDao;
    private final RecordingBlobDaoBase<RecordingBlobData> recordingBlobDao;
    private final BlobHelper blobHelper;

    public RecordingService(RecordingSessionDaoBase<RecordingSessionData> recordingSessionDao, RecordingLogDaoBase<RecordingLogData> recordingLogDao,
                            RecordingBlobDaoBase<RecordingBlobData> recordingBlobDao, BlobHelper blobHelper) {

        assert recordingSessionDao != null;
        assert recordingLogDao != null;
        assert recordingBlobDao != null;
        assert blobHelper != null;

        this.blobHelper = blobHelper;
        this.recordingSessionDao = recordingSessionDao;
        this.recordingLogDao = recordingLogDao;
        this.recordingBlobDao = recordingBlobDao;
    }

    @Override
    @Transactional
    public void recordLog(RecordingLogData recordingLog) {
        recordingLogDao.persist(recordingLog);
    }

    @Override
    @Transactional
    public void recordLog(RecordingLogData recordingLog, long blobId, List<String> blobs) {

        int i = 0;

        for (String blob : blobs) {
            RecordingBlobData recordingBlob = recordingBlobDao.getEntityInstance();
            recordingBlob.setId(new JpaRecordingBlobPk());
            recordingBlob.getId().setSessionId(recordingLog.getSession().getId());
            recordingBlob.getId().setBlobId(blobId + i++ + 1);
            recordingBlob.setContent(blobHelper.createBlob(blob.getBytes(Charset.forName("UTF-8"))));

            recordingBlobDao.persist(recordingBlob);
        }

        recordingLogDao.persist(recordingLog);
    }

    @Override
    @Transactional
    public RecordingSessionData startSession(RecordingSessionData session) {
        return recordingSessionDao.persist(session);
    }
}