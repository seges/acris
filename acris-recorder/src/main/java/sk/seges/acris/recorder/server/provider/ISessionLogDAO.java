package sk.seges.acris.recorder.server.provider;

import java.util.List;

import sk.seges.acris.recorder.server.domain.SessionLog;
import sk.seges.acris.security.shared.user_management.model.dto.GenericUserDTO;

public interface ISessionLogDAO {
	SessionLog add(SessionLog log);

	List<SessionLog> load(String sessionId);
	List<GenericUserDTO> loadUsers();
	List<SessionLog> load();
	List<SessionLog> load(List<String> sessionIds);
}