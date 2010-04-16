package sk.seges.acris.recorder.server.provider;

import java.util.List;

import sk.seges.acris.recorder.rpc.domain.SessionLog;
import sk.seges.acris.security.rpc.user_management.domain.GenericUser;

public interface ISessionLogDAO {
	public SessionLog add(SessionLog log);

	public List<SessionLog> load(String sessionId);
	public List<GenericUser> loadUsers();
	public List<SessionLog> load();
	public List<SessionLog> load(List<String> sessionIds);
}