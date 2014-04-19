package sk.seges.acris.recorder.shared.service;

import com.google.gwt.user.client.rpc.RemoteService;
import sk.seges.acris.recorder.shared.model.dto.RecordingLogDTO;
import sk.seges.acris.recorder.shared.model.dto.RecordingSessionDTO;
import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;

import java.util.List;

/**
 * Audit trail is a chronological sequence of audit records, each of which
 * contains evidence user activity. We are interesting of: 
 * - mouse events (mouse click, mouse over, mouse leave, ...) 
 * - keyboard events (keydown, keypress, keyup) 
 * - html events (blur, focus, contextmenu, change, ...)
 * 
 * @author fat
 * 
 */
@RemoteServiceDefinition
public interface IRecordingRemoteService extends RemoteService {

    RecordingSessionDTO startSession(RecordingSessionDTO session);

    void recordLog(RecordingLogDTO recordingLog);

	void recordLog(RecordingLogDTO recordingLog, long blobId, List<String> blobs);
}