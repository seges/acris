package sk.seges.acris.player.shared.service;

import com.google.gwt.user.client.rpc.RemoteService;
import sk.seges.acris.recorder.shared.model.dto.RecordingLogDTO;
import sk.seges.acris.recorder.shared.model.dto.RecordingSessionDTO;
import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;
import sk.seges.sesam.shared.model.dto.PageDTO;
import sk.seges.sesam.shared.model.dto.PagedResultDTO;

import java.util.List;

@RemoteServiceDefinition
public interface IPlayerRemoteService extends RemoteService {

	PagedResultDTO<List<RecordingSessionDTO>> getSessions(PageDTO page, String webId, String language);

	PagedResultDTO<List<RecordingLogDTO>> getLogs(PageDTO page, RecordingSessionDTO sessions);

	RecordingSessionDTO getSession(long sessionId);
}