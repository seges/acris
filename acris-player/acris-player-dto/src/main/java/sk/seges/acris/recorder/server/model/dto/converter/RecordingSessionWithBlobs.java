package sk.seges.acris.recorder.server.model.dto.converter;

import sk.seges.acris.recorder.RecordingSession;
import sk.seges.acris.recorder.server.model.data.RecordingBlobData;
import sk.seges.acris.recorder.server.model.data.RecordingSessionData;

import java.util.List;

/**
 * Created by PeterSimun on 14.4.2014.
 */
public class RecordingSessionWithBlobs {

    private RecordingSessionData recordingSession;
    private List<RecordingBlobData> recordingBlobs;

    public RecordingSessionWithBlobs() {}

    public RecordingSessionWithBlobs(RecordingSessionData recordingSession, List<RecordingBlobData> recordingBlobs) {
        this.recordingSession = recordingSession;
        this.recordingBlobs = recordingBlobs;
    }

    public RecordingSessionData getRecordingSession() {
        return recordingSession;
    }

    public void setRecordingSession(RecordingSessionData recordingSession) {
        this.recordingSession = recordingSession;
    }

    public List<RecordingBlobData> getRecordingBlobs() {
        return recordingBlobs;
    }

    public void setRecordingBlobs(List<RecordingBlobData> recordingBlobs) {
        this.recordingBlobs = recordingBlobs;
    }
}
