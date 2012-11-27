package sk.seges.acris.recorder.server.provider;

import java.util.List;

import sk.seges.acris.recorder.server.domain.SessionLog;
import sk.seges.acris.security.shared.user_management.model.dto.GenericUserDTO;

public interface ISessionLogDAO {
	public SessionLog add(SessionLog log);

	public List<SessionLog> load(String sessionId);
	public List<GenericUserDTO> loadUsers();
	public List<SessionLog> load();
	public List<SessionLog> load(List<String> sessionIds);
}