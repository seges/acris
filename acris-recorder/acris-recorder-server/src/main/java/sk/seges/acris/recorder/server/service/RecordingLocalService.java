package sk.seges.acris.recorder.server.service;

import sk.seges.acris.recorder.server.model.data.RecordingSessionData;

import java.util.List;

public interface RecordingLocalService extends IRecordingRemoteServiceLocal {

	List<RecordingSessionData> getRecordingSessions();
}
