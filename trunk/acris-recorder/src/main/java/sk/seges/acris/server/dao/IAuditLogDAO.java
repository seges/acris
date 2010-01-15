package sk.seges.acris.server.dao;

import java.util.List;

import sk.seges.acris.server.domain.AuditLog;

public interface IAuditLogDAO {
	public AuditLog add(AuditLog log);

	public List<AuditLog> load();
}