package sk.seges.acris.recorder.server.dao;

import java.util.List;

import sk.seges.acris.recorder.server.domain.AuditLog;

public interface IAuditLogDAO {
	public AuditLog add(AuditLog log);

	public List<AuditLog> load();
}